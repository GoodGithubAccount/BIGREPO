package project.github.backend.order

import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.RepresentationModelAssembler
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.stereotype.Component

/**
 * A [RepresentationModelAssembler] implementation that converts the domain type [Order] object
 * into its [EntityModel] representation.
 *
 * The class is marked as [Component] and will automatically be created on startup.
 */
@Component
class OrderModelAssembler : RepresentationModelAssembler<Order, EntityModel<Order>> {

    /**
     * Converts the non-model object [order] to the equivalent model-based object
     * [EntityModel] of the [order] including the self link and link to all contained products.
     * If the order [Status] is 'IN_PROGRESS' an additional 'cancel' and 'complete' href will be provided
     *
     * @param order the non-model object to be converted.
     * @return the converted model-based object.
     */
    override fun toModel(order: Order): EntityModel<Order> {
        val orderModel = EntityModel.of(
            order,
            linkTo(methodOn(OrderController::class.java).getOrder(order.getId())).withSelfRel(),
            linkTo(methodOn(OrderController::class.java).all()).withRel("orders")
        )
        if (order.getStatus() == Status.IN_PROGRESS) {
            orderModel.add(linkTo(methodOn(OrderController::class.java).cancel(order.getId())).withRel("cancel"))
            orderModel.add(linkTo(methodOn(OrderController::class.java).complete(order.getId())).withRel("complete"))
        }
        return orderModel
    }
}