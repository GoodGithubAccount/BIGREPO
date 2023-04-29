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
     * Saves a [Product] to the database.
     * @param product the [Product] to be saved.
     * @return the saved [Product].
     */
    fun save(product: Product): Product {
        return productRepository.save(product)
    }

    /**
     * Deletes a [Product] from the database.
     * @param id the id of the [Product] to be deleted.
     */
    fun delete(id: String) {
        productRepository.deleteById(id)
    }

    /**
     * Updates all [Product] fields from the [existingProduct] to the [newProduct].
     * @param  existingProduct the [Product] to be updated.
     * @param  newProduct the [Product] with the new values.
     * @return the updated [Product].
     */
    fun updateProduct(existingProduct: Product, newProduct: Product) {
        existingProduct.name = newProduct.name
        existingProduct.price = newProduct.price
        existingProduct.currency = newProduct.currency
        existingProduct.rebateQuantity = newProduct.rebateQuantity
        existingProduct.rebatePercent = newProduct.rebatePercent
        existingProduct.upsellProduct = newProduct.upsellProduct
    }

    /**
     * Fetches a [Product] by its id from the database.
     * @param id the id of the product.
     * @return the product with the given id.
     * @throws ProductNotFoundException if no product with the given id is found.
     */
    fun getProduct(id: String): Product {
        return productRepository.findById(id).orElseThrow { ProductNotFoundException(id) }
    }

    /**
     * Fetches all [Product]s from the database.
     * @return the fetched [Product]s.
     */
    fun getAllProducts(): List<Product> {
        return productRepository.findAll()
    }
}