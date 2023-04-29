package project.github.backend.basket

import org.springframework.stereotype.Service
import project.github.backend.basket.exception.BasketNotFoundException
import project.github.backend.basketproduct.BasketProductService
import project.github.backend.order.exceptions.IllegalDuplicateProductException
import java.math.BigDecimal

/**
 * Service that provides [Basket] related operations with the database.
 */
@Service
class BasketService(
    private val basketRepository: BasketRepository, private val basketProductService: BasketProductService
) {
    fun getBasket(id: Long): Basket {
        return this.basketRepository.findById(id).orElseThrow { BasketNotFoundException(id) }
    }

    /**
     * Creates a new [Basket] entity.
     * @param  products the products to add to the basket where the key is the product id and the value is the quantity.
     * @return  the created basket.
     */
    fun createBasket(products: Map<String, Int>): Basket {
        val basketProducts = products.map { (id, quantity) ->
            if (products.count { it.key == id } > 1) {
                throw IllegalDuplicateProductException(id)
            }
            basketProductService.createBasketProduct(id, quantity)
        }
        return Basket(
            products = basketProducts, numberOfProducts = basketProducts.size
        ).also { basketRepository.save(it) }
    }

    /**
     * Returns the total price of the [Basket]
     * @param id the id of the basket.
     * @return the total price of the basket.
     */
    fun getTotalPrice(id: Long): BigDecimal {
        return getBasket(id).products.map { basketProduct ->
            basketProduct.price * basketProduct.quantity.toBigDecimal()
        }.reduce { sum, price -> sum + price }
    }
}