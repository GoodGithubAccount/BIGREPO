package project.github.backend.entity.basket

import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.RepresentationModelAssembler
import org.springframework.stereotype.Component

/**
 * A [RepresentationModelAssembler] implementation that converts the domain type
 * [Basket] object into its [EntityModel] representation.
 */
@Component
class BasketModelAssembler : RepresentationModelAssembler<Basket, EntityModel<Basket>> {
    override fun toModel(entity: Basket): EntityModel<Basket> {
        TODO("Not yet implemented")
    }

}