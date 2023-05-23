package project.github.backend.entity.basket

import org.springframework.hateoas.mediatype.hal.HalModelBuilder
import org.springframework.hateoas.server.ExposesResourceFor
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import project.github.backend.entity.basket.exception.BasketNotFoundException
import project.github.backend.entity.order.OrderController
import project.github.backend.entity.product.ProductController

@RestController
@ExposesResourceFor(Basket::class)
@RequestMapping("/basket")
class BasketController(
    private val basketService: BasketService,
    private val basketAssembler: BasketModelAssembler,
    private val basketProductAssembler: BasketProductModelAssembler
) {

    @GetMapping("/{id}")
    fun getBasket(@PathVariable id: Long?): HttpEntity<*> {
        if (id == null) {
            throw BasketNotFoundException(null)
        }

        val basket = basketService.getBasket(id)

        val proxyControllerClass = methodOn(BasketController::class.java)

        val selfLink = linkTo(proxyControllerClass.getBasket(id)).withSelfRel()
        val orderLink = linkTo(methodOn(OrderController::class.java).getOrder(basket.order!!.id!!)).withRel("order")
        val basketProductLinks = basket.products.map { product ->
            linkTo(proxyControllerClass.getBasketProduct(id, product.id!!)).withRel("product")
                .withTitle(product.id.toString())
        }

        val links = listOf(selfLink, orderLink).plus(basketProductLinks)

        val model = HalModelBuilder.halModel()
            .embed(BasketPreview(basket))
            .links(links)
            .build()

        return ResponseEntity.ok(model)
    }

    class BasketPreview(basket: Basket) {
        val numberOfProducts = basket.numberOfProducts
    }

    @GetMapping("/{basketId}/products/{productId}")
    fun getBasketProduct(@PathVariable basketId: Long, @PathVariable productId: Long): EntityModel<BasketProduct> {
        val basketProduct = basketService.getBasketProduct(basketId, productId)
        return basketProductAssembler.toModel(basketProduct)
    }
}