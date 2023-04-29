package project.github.backend.entity.order

import org.springframework.hateoas.RepresentationModel


/**
 * TODO
 */
class OrderRepresentation : RepresentationModel<OrderRepresentation>() {
    val currency: String? = null
    val status: Status? = null
    val products: Map<String, Int>? = null
}