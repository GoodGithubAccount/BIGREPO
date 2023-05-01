package project.github.backend.entity.basket

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.RepresentationModelAssembler
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.stereotype.Component
import project.github.backend.entity.basket.basketproduct.BasketProductModelAssembler
import project.github.backend.entity.order.OrderController

/**
 * A [RepresentationModelAssembler] implementation that converts the domain type
 * [Basket] object into its [EntityModel] representation.
 */
@Component
class BasketModelAssembler : RepresentationModelAssembler<Basket, EntityModel<Basket>> {
    @Autowired
    private lateinit var basketProductModelAssembler: BasketProductModelAssembler
    override fun toModel(basket: Basket): EntityModel<Basket> {
        val model =
            EntityModel.of(basket.copy(products = basket.products.map { basketProductModelAssembler.toModel(it).content!! }))
        model.addSelfRel()
        model.addOrderRel()
        return model
    }

    private fun EntityModel<Basket>.addSelfRel() {
        val basketId = this.content!!.id!!

        val controllerClass = BasketController::class.java

        add(
            linkTo(
                methodOn(controllerClass).getBasket(basketId)
            ).withSelfRel()
        )
    }

    private fun EntityModel<Basket>.addOrderRel() {
        val orderId = this.content!!.order!!.id!!
        add(linkTo(methodOn(OrderController::class.java).getOrder(orderId)).withRel("order"))
    }

    private fun EntityModel<Basket>.addBasketProductRel() {
        TODO("Not yet implemented")
        //Seems difficult to add a basket product relation to a basket, because the basket product is not a resource
    }
}