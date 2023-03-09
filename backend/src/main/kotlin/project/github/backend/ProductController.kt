package project.github.backend

import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.DeleteMapping

@RestController
class ProductController(private val repository: ProductRepository) {

    /**
     * Returns all instances of [Product] in the [ProductRepository].
     *
     * @return all [Product] entities
     */
    @GetMapping("/products")
    fun all(): List<Product> {
        return this.repository.findAll()
    }

    /**
     * Endpoint for saving a given [Product] instance to the [ProductRepository].
     * @param newProduct The [Product] entity to be saved.
     * @return The saved [Product].
     */
    @PostMapping("/products")
    fun newProduct(@RequestBody newProduct: Product): Product {
        return this.repository.save(newProduct)
    }


    //Single items

    /**
     * Returns a [Product] by its [id]
     *
     * @throws ProductNotFoundException if the [id] could not be found in the [ProductRepository]
     */
    @GetMapping("/products/{id}")
    fun getProduct(@PathVariable id: String): Product {
        return this.repository.findById(id).orElseThrow { ProductNotFoundException(id) }
    }

    /**
     * Replaces an existing [Product] by its [id] with [newProduct].
     *
     * If the [Product] is not found the [newProduct] is saved in the [ProductRepository].
     */
    @PutMapping("/products/{id}")
    fun replaceProduct(@RequestBody newProduct: Product, @PathVariable id: String): Product {
        return this.repository.findById(id).map { existingProduct: Product ->
            updateProductFrom(existingProduct, newProduct)
            this.repository.save(existingProduct)
        }.orElse(this.repository.save(newProduct))
    }

    /**
     * Updates all [Product] fields from the [newProduct] to the [existingProduct]
     */
    private fun updateProductFrom(newProduct: Product, existingProduct: Product) {
        existingProduct.setName(newProduct.getName())
        existingProduct.setPrice(newProduct.getPrice())
        existingProduct.setCurrency(newProduct.getCurrency())
        existingProduct.setRebateQuantity(newProduct.getRebateQuantity())
        existingProduct.setRebatePercent(newProduct.getRebatePercent())
        existingProduct.setUpsellProduct(newProduct.getUpsellProduct())
    }

    /**
     * Deletes a [Product] with the given [id].
     *
     * If the [Product] is not found in the [ProductRepository] it is silently ignored.
     */
    @DeleteMapping("/products/{id}")
    fun deleteProduct(@PathVariable id: String) {
        this.repository.deleteById(id)
    }
}