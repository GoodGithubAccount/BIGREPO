package project.github.backend.entity.product

import org.springframework.stereotype.Service

/**
 * Service that provides [Product] related operations with the database.
 */
@Service
class ProductService(
    private val productRepository: ProductRepository
) {
    /**
     * Fetches a [Product] by its id from the database.
     * @param id the id of the product.
     * @return the product with the given id.
     * @throws ProductNotFoundException if no product with the given id is found.
     */
    fun getProduct(id: String): Product {
        return productRepository.findById(id).orElseThrow { ProductNotFoundException(id) }
    }
}