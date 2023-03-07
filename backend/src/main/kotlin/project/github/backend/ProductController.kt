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
     * Saves a given [Product] instance to the [ProductRepository].
     *
     * @param newProduct the [Product] entity to be saved
     * @return the saved [Product]
     */
    @PostMapping("/products")
    fun newProduct(@RequestBody newProduct: Product): Product {
        return this.repository.save(newProduct)
    }


    //Single items

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
        return this.repository.findById(id).map { product: Product ->
            product.setName(newProduct.getName())
            product.setPrice(newProduct.getPrice())
            product.setCurrency(newProduct.getCurrency())
            product.setRebateQuantity(newProduct.getRebateQuantity())
            product.setRebatePercent(newProduct.getRebatePercent())
            product.setUpsellProduct(newProduct.getUpsellProduct())
            this.repository.save(product)
        }.orElseGet {
            //newProduct.setId(id)?
            this.repository.save(newProduct)
        }
    }

    /**
     * Deletes a [Product] with the given [id].
     *
     * If the [Product] is not found in the [ProductRepository] it is silently ignored.
     *
     */
    @DeleteMapping("/products/{id}")
    fun deleteProduct(@PathVariable id: String) {
        this.repository.deleteById(id)
    }
}