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
     * @param productRepresentation the [Product] to be saved in the form of a [ProductRepresentation].
     * @return the saved [Product].
     */
    fun save(productRepresentation: ProductRepresentation): Product {
        val newProduct = Product(
            id = productRepresentation.id!!,
            name = productRepresentation.name!!,
            price = productRepresentation.price!!.toInt(),
            currency = productRepresentation.currency!!,
            rebateQuantity = productRepresentation.rebateQuantity!!,
            rebatePercent = productRepresentation.rebatePercent!!.toInt(),
            upsellProduct = productRepresentation.upsellProductId
            )
        return save(newProduct)
    }

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
     * Updates all [Product] fields from the new product to the existing product with the given id.
     * @param  existingProductId the ID of the [Product] to be updated.
     * @param  newProduct the [Product] with the new values.
     * @return the updated [Product].
     */
    fun updateProduct(existingProductId: String, newProduct: ProductRepresentation): Product {
        val existingProduct = getProduct(existingProductId)
        existingProduct.name = newProduct.name!!
        existingProduct.price = newProduct.price!!.toInt()
        existingProduct.currency = newProduct.currency!!
        existingProduct.rebateQuantity = newProduct.rebateQuantity!!
        existingProduct.rebatePercent = newProduct.rebatePercent!!.toInt()
        existingProduct.upsellProduct = newProduct.upsellProductId
        return existingProduct
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