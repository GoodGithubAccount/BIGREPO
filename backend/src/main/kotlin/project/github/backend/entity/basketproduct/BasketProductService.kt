package project.github.backend.entity.basketproduct

import org.springframework.stereotype.Service
import project.github.backend.entity.basketproduct.exception.BasketProductNotFoundException
import project.github.backend.entity.product.ProductService

/**
 * Service that provides [BasketProduct] related operations with the database.
 */
@Service
class BasketProductService(
    private val basketProductRepository: BasketProductRepository, private val productService: ProductService
) {
    /**
     * Fetches a [BasketProduct] by its id from the database.
     * @param id The id of the [BasketProduct].
     * @return The [BasketProduct] with the given id.
     * @throws BasketProductNotFoundException if no [BasketProduct] with the given id is found.
     */
    fun getBasketProduct(id: Long) {
        basketProductRepository.findById(id).orElseThrow { BasketProductNotFoundException(id) }
    }

    /**
     * Creates a [BasketProduct] and saves it to the database.
     * @param productId The id of the product to add to the basket.
     * @param quantity The quantity of the product to add to the basket.
     * @return The created [BasketProduct].
     */
    fun createBasketProduct(productId: String, quantity: Int): BasketProduct {
        val foundProduct = productService.getProduct(productId)
        return BasketProduct(
            productId = productId,
            quantity = quantity,
            price = foundProduct.price.toBigDecimal(),
            currency = foundProduct.currency
        ).also { basketProductRepository.save(it) }
    }
}