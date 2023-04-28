package project.github.backend.order

import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.RepresentationModelAssembler
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*
import org.springframework.stereotype.Component
import project.github.backend.basket.BasketController

/**
 * A [RepresentationModelAssembler] implementation that converts the domain type [Order] object
 * into its [EntityModel] representation.
 *
 * The class is marked as [Component] and will automatically be created on startup.
 */
@Component
class OrderModelAssembler : RepresentationModelAssembler<Order, EntityModel<Order>> {

    /**
     * Converts the [Order] object into its [EntityModel] representation.
     * The [EntityModel] is contains the links to the related resources.
     * @param order the domain object to be converted.
     * @return the [EntityModel] representation with the links.
     */
    override fun toModel(order: Order): EntityModel<OrderJson> {
        val orderItemsWithLinks = order.orderItems.map { orderItem ->
            val orderItemLinks = linkTo(methodOn(ProductController::class.java).getProduct(orderItem.product.getId())).withRel("product")
            EntityModel.of(OrderItemJson(orderItem.id!!, orderItem.product, orderItem.quantity, mapOf("product" to orderItemLinks)))
        }

private fun EntityModel<Order>.addSelfRel() {
    val orderId = this.content!!.id!!

    val controllerClass = OrderController::class.java

    // with affordance to createOrder method
    add(linkTo(methodOn(OrderController::class.java).getOrder(orderId)).withSelfRel()
        .andAffordance(afford(methodOn(controllerClass).createOrder(null))))
}

private fun EntityModel<Order>.addBasketRel() {
    val basketId = this.content!!.basket.id!!
    add(linkTo(methodOn(BasketController::class.java).getBasket(basketId)).withRel("basket"))
}