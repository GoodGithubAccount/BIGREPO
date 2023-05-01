package project.github.backend.entity.basket

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import project.github.backend.entity.basket.basketproduct.BasketProduct
import project.github.backend.entity.basket.exception.BasketNotFoundException
import project.github.backend.entity.basket.basketproduct.BasketProductService
import project.github.backend.entity.order.Order
import java.math.BigDecimal

/**
 * Service that provides [Basket] related operations with the database.
 */
@Service
class BasketService(
    private val basketRepository: BasketRepository,
    private val basketProductService: BasketProductService,
) {
    private val log: Logger = LoggerFactory.getLogger(BasketService::class.java)

    fun getBasket(id: Long): Basket {
        return this.basketRepository.findById(id).orElseThrow { BasketNotFoundException(id) }
    }

    /**
     * Creates a new [Basket] entity.
     * @param  products the products to add to the basket where the key is the product id and the value is the quantity.
     * @return  the created basket.
     */
    fun createBasket(order: Order, products: Map<String, Int>): Basket {
        var basket = Basket(products = emptyList(), numberOfProducts = 0).also { basketRepository.save(it) }
        val basketProducts = products.map { (productId, quantity) ->
            basketProductService.createBasketProduct(basket, productId, quantity)
        }
        basket.products = basketProducts
        basket.numberOfProducts = basketProducts.size
        basket.order = order

        basket = basketRepository.save(basket)
        log.info("Basket created: $basket")
        return basket
    }

    /**
     * Returns the total price of the [Basket]
     * @param id the id of the basket.
     * @return the total price of the basket.
     */
    fun getTotalPrice(id: Long): BigDecimal {
        if (getBasket(id).products.isEmpty()) {
            return BigDecimal.ZERO
        }

        return getBasket(id).products.map { basketProduct ->
            basketProduct.price * basketProduct.quantity.toBigDecimal()
        }.reduce { sum, price -> sum + price }
    }

    fun getBasketProduct(basketId: Long, productId: Long): BasketProduct {
        return basketProductService.getBasketProduct(basketId, productId)
    }
}