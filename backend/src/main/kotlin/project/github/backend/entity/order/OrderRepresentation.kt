package project.github.backend.entity.order

import org.springframework.hateoas.RepresentationModel


/**
 * The [RepresentationModel] of an [Order].
 * It is the form in which the client uses to create an order.
 */
class OrderRepresentation : RepresentationModel<OrderRepresentation>() {
    var products: Map<String, Int>? = null
}