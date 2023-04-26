package project.github.backend.order

import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.RepresentationModelAssembler
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.stereotype.Component
import project.github.backend.order.OrderModelAssembler.*
import project.github.backend.product.Product
import project.github.backend.product.ProductController

/**
 * A [RepresentationModelAssembler] implementation that converts the domain type [Order] object
 * into its [EntityModel] representation.
 *
 * The class is marked as [Component] and will automatically be created on startup.
 */
@Component
class OrderModelAssembler : RepresentationModelAssembler<Order, EntityModel<OrderJson>> {

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

        val orderLinks = mutableMapOf(
            "self" to linkTo(methodOn(OrderController::class.java).getOrder(order.id!!)).withSelfRel(),
        )

        if (order.getStatus() == Status.IN_PROGRESS) {
            orderLinks["cancel"] = linkTo(methodOn(OrderController::class.java).cancel(order.id)).withRel("cancel")
            orderLinks["complete"] = linkTo(methodOn(OrderController::class.java).complete(order.id)).withRel("complete")
        }

        val orderJson = OrderJson(order.id, order.getStatus().name, orderItemsWithLinks, orderLinks)

        return EntityModel.of(orderJson)
    }

    data class OrderJson(
        val id: Long,
        val status: String,
        val orderItems: List<EntityModel<OrderItemJson>>,
        val _links: Map<String, Any>
    )

    data class OrderItemJson(
        val id: Long,
        val product: Product,
        val quantity: Int,
        val _links: Map<String, Any>
    )
}