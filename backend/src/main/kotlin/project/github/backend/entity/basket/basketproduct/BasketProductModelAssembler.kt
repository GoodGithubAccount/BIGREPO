package project.github.backend.entity.basket.basketproduct

import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.RepresentationModelAssembler
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.stereotype.Component
import project.github.backend.entity.basket.BasketController
import project.github.backend.entity.product.ProductController

/**
 * A [RepresentationModelAssembler] implementation that converts the domain type
 * [BasketProduct] object into its [EntityModel] representation.
 *
 */
@Component
class BasketProductModelAssembler : RepresentationModelAssembler<BasketProduct, EntityModel<BasketProduct>> {
    override fun toModel(basketProduct: BasketProduct): EntityModel<BasketProduct> {
        val model = EntityModel.of(basketProduct)
        model.addSelfRel()
        model.addBasketRel()
        model.addProductRel()

        return model
    }
}

private fun EntityModel<BasketProduct>.addSelfRel() {
    val basketProductId = this.content!!.id!!
    val basketId = this.content!!.basket.id!!

    val controllerClass = BasketController::class.java

    add(
        WebMvcLinkBuilder.linkTo(
            WebMvcLinkBuilder.methodOn(controllerClass).getBasketProduct(basketId, basketProductId)
        ).withSelfRel()
    )
}

private fun EntityModel<BasketProduct>.addBasketRel() {
    val basketId = this.content!!.basket.id!!
    add(
        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(BasketController::class.java).getBasket(basketId))
            .withRel("basket")
    )
}

private fun EntityModel<BasketProduct>.addProductRel() {
    val productId = this.content!!.productId
    add(
        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProductController::class.java).getProduct(productId))
            .withRel("product")
    )
}
