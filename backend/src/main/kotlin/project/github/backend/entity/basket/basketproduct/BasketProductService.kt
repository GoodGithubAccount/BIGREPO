package project.github.backend.entity.basket.basketproduct

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import project.github.backend.entity.basket.Basket
import project.github.backend.entity.basket.basketproduct.exception.BasketProductNotFoundException
import project.github.backend.entity.product.ProductService

/**
 * Service that provides [BasketProduct] related operations with the database.
 */
@Service
class BasketProductService(
    private val basketProductRepository: BasketProductRepository, private val productService: ProductService
) {
    private val log: Logger = LoggerFactory.getLogger(BasketProductService::class.java)

    /**
     * Fetches a [BasketProduct] by its id from the database.
     * @param basketId The id of the related [Basket].
     * @param basketProductId The id of the [BasketProduct].
     * @return The [BasketProduct] with the given id.
     * @throws BasketProductNotFoundException if no [BasketProduct] with the given id is found.
     */
    fun getBasketProduct(basketId: Long, basketProductId: Long): BasketProduct {
        return basketProductRepository.findByBasketIdAndId(basketId, basketProductId)
            .orElseThrow { BasketProductNotFoundException(basketId, basketProductId) }
    }

    /**
     * Creates a [BasketProduct] and saves it to the database.
     * @param productId The id of the product to add to the basket.
     * @param quantity The quantity of the product to add to the basket.
     * @return The created [BasketProduct].
     */
    fun createBasketProduct(basket: Basket, productId: String, quantity: Int): BasketProduct {
        val foundProduct = productService.getProduct(productId)
        val basketProduct = BasketProduct(
            productId = productId,
            quantity = quantity,
            price = foundProduct.price.toBigDecimal(),
            currency = foundProduct.currency,
            basket = basket
        ).also { basketProductRepository.save(it) }

        log.info("BasketProduct created: $basketProduct")
        return basketProduct
    }
}