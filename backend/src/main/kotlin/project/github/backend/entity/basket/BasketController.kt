package project.github.backend.entity.basket

import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.ExposesResourceFor
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import project.github.backend.entity.basket.basketproduct.BasketProduct
import project.github.backend.entity.basket.basketproduct.BasketProductModelAssembler

@RestController
@ExposesResourceFor(Basket::class)
@RequestMapping("/basket")
class BasketController(
    private val basketService: BasketService,
    private val basketAssembler: BasketModelAssembler,
    private val basketProductAssembler: BasketProductModelAssembler
) {

    @GetMapping("/{id}")
    fun getBasket(@PathVariable id: Long): EntityModel<Basket> {
        val basket = basketService.getBasket(id)
        return basketAssembler.toModel(basket)
    }

    @GetMapping("/{basketId}/products/{productId}")
    fun getBasketProduct(@PathVariable basketId: Long, @PathVariable productId: Long): EntityModel<BasketProduct> {
        val basketProduct = basketService.getBasketProduct(basketId, productId)
        return basketProductAssembler.toModel(basketProduct)
    }
}