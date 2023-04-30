package project.github.backend.entity.order

import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.RepresentationModelAssembler
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*
import org.springframework.stereotype.Component
import project.github.backend.entity.basket.BasketController

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
    override fun toModel(order: Order): EntityModel<Order> {
        val model = EntityModel.of(order)
        model.addSelfRel()
        model.addBasketRel()

        return model
    }
}

private fun EntityModel<Order>.addSelfRel() {
    val orderId = this.content!!.id!!

    val controllerClass = OrderController::class.java

    add(linkTo(methodOn(controllerClass).getOrder(orderId)).withSelfRel()
        .andAffordance(afford(methodOn(controllerClass).createOrder(null))))
}

private fun EntityModel<Order>.addBasketRel() {
    val basketId = this.content!!.basket.id!!
    add(linkTo(methodOn(BasketController::class.java).getBasket(basketId)).withRel("basket"))
}