package project.github.backend

import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.IanaLinkRelations
import org.springframework.hateoas.server.core.DummyInvocationUtils.methodOn
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.DeleteMapping
import java.util.stream.Collectors

/**
 * This class represents the [RestController] for handling HTTP requests related to [Product] entities.
 * It defines several endpoints for managing CRUD operations of the [Product] entities.
 * @param repository The [ProductRepository] instance used to interact with the data store.
 * @param assembler The [ProductModelAssembler] instance used to convert [Product] entities to [EntityModel]s.
 */
@RestController
class ProductController(private val repository: ProductRepository, private val assembler: ProductModelAssembler) {

    /**
     * Endpoint for Retrieving all [Product]s from the [repository] and returns them as a collection of [EntityModel]s.
     * @return A [CollectionModel] containing [EntityModel]s of all products in the [repository],
     * along with a self-referencing link.
     */
    @GetMapping("/products")
    fun all(): CollectionModel<EntityModel<Product>> {
        val productsStream = this.repository.findAll().stream()
        val productsAsEntityModels = productsStream.map {
            product ->
            convertToEntityModel(product)
        }.collect(Collectors.toList())

        return CollectionModel.of(productsAsEntityModels,
                linkTo(methodOn(ProductController::class.java).all()).withSelfRel()
        )
    }

    /**
     * Endpoint for saving a given [Product] instance to the [ProductRepository].
     * @param newProduct The [Product] entity to be saved.
     * @return A [ResponseEntity] object with the saved [Product] in the response body
     * and a self-referencing link in the response header.
     * The response status code is 201 (Created)
     */
    @PostMapping("/products")
    fun newProduct(@RequestBody newProduct: Product): ResponseEntity<*> {
        val entityModel: EntityModel<Product> = assembler.toModel(repository.save(newProduct))
        return ResponseEntity
            .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
            .body<EntityModel<Product>>(entityModel)
    }


    //Single items

    /**
     * Endpoint for retrieving the details of a [Product] with the specified ID.
     * @param id The ID of the [Product] to retrieve.
     * @return An [EntityModel] containing the details of the retrieved product.
     * @throws ProductNotFoundException If no product with the specified [id] is found in the [ProductRepository].
     */
    @GetMapping("/products/{id}")
    fun getProduct(@PathVariable id: String): EntityModel<Product> {
        val product = findProductById(id).orElseThrow { ProductNotFoundException(id) }
        return convertToEntityModel(product)
    }

    /**
     * Finds a [Product] in the [repository] by its ID.
     * @param id The ID of the product to find.
     * @return The product with the specified ID if it exists, Optional#empty() otherwise.
     */
    private fun findProductById(id: String) = this.repository.findById(id)

    /**
     * Converts the non-model object [product] to the equivalent
     * model-based object [EntityModel] using the class [assembler].
     * @param product The product object to convert.
     * @return An [EntityModel] containing the details of the converted product.
     */
    private fun convertToEntityModel(product: Product) =
            this.assembler.toModel(product)

    /**
     * Endpoint for updating an existing [Product] by its [id] with [newProduct].
     *
     * If the [Product] is not found the [newProduct] is saved in the [ProductRepository].
     */
    @PutMapping("/products/{id}")
    fun replaceProduct(@RequestBody newProduct: Product, @PathVariable id: String): Product {
        return findProductById(id).map { existingProduct: Product ->
            updateProductFrom(existingProduct, newProduct)
            this.repository.save(existingProduct)
        }.orElse(this.repository.save(newProduct))
    }

    /**
     * Updates all [Product] fields from the [existingProduct] to the [newProduct].
     */
    private fun updateProductFrom(existingProduct: Product, newProduct: Product) {
        existingProduct.setName(newProduct.getName())
        existingProduct.setPrice(newProduct.getPrice())
        existingProduct.setCurrency(newProduct.getCurrency())
        existingProduct.setRebateQuantity(newProduct.getRebateQuantity())
        existingProduct.setRebatePercent(newProduct.getRebatePercent())
        existingProduct.setUpsellProduct(newProduct.getUpsellProduct())
    }

    /**
     * Endpoint for deleting a [Product] with the given [id].
     *
     * If the [Product] is not found in the [ProductRepository] it is silently ignored.
     */
    @DeleteMapping("/products/{id}")
    fun deleteProduct(@PathVariable id: String) {
        this.repository.deleteById(id)
    }
}