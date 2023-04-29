package project.github.backend.basket

import org.springframework.hateoas.EntityModel
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import project.github.backend.basketproduct.BasketProduct

@RestController
@RequestMapping("/basket")
class BasketController {

    @GetMapping("/{id}")
    fun getBasket(@PathVariable id: Long): EntityModel<Basket> {
        TODO("Not yet implemented")
    }

    @GetMapping("/{basketId}/products/{productId}")
    fun getBasketProduct(@PathVariable basketId: String, @PathVariable productId: String): EntityModel<BasketProduct> {
        TODO("Not yet implemented")
    }
}