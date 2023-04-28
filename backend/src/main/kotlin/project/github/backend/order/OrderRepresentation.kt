package project.github.backend.order

import org.springframework.hateoas.RepresentationModel


class OrderRepresentation : RepresentationModel<OrderRepresentation>() {
    val currency: String? = null
    val status: Status? = null
    val products: Map<String, Int>? = null
}