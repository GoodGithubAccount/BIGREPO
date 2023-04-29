package project.github.backend.entity.product

import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.IanaLinkRelations
import org.springframework.hateoas.server.core.DummyInvocationUtils.methodOn
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.stream.Collectors

/**
 * This class represents the [RestController] for handling HTTP requests related to [Product] entities.
 * It defines several endpoints for managing CRUD operations of the [Product] entities.
 * @param assembler The [ProductModelAssembler] instance used to convert [Product] entities to [EntityModel]s.
 * @param productService The [ProductService] instance used to perform CRUD operations on [Product] entities.
 */
@RestController
@RequestMapping("/products")
class ProductController(
    private val assembler: ProductModelAssembler, private val productService: ProductService
) {

    /**
     * Endpoint for Retrieving all [Product]s from the database and returns them as a collection of [EntityModel]s.
     * @return A [CollectionModel] containing [EntityModel]s of all products in the database,
     * along with a self-referencing link.
     */
    @GetMapping()
    fun all(): CollectionModel<EntityModel<Product>> {
        val productsStream = this.productService.getAllProducts().stream()
        val productsAsEntityModels = productsStream.map { product ->
            convertToEntityModel(product)
        }.collect(Collectors.toList())

        return CollectionModel.of(
            productsAsEntityModels, linkTo(methodOn(ProductController::class.java).all()).withSelfRel()
        )
    }

    /**
     * Endpoint for saving a given [Product] instance to the [ProductRepository].
     * @param newProduct The [Product] entity to be saved.
     * @return A [ResponseEntity] object with the saved [Product] in the response body
     * and a self-referencing link in the response header.
     * The response status code is 201 (Created).
     */
    @PostMapping()
    fun newProduct(@RequestBody newProduct: ProductRepresentation): ResponseEntity<*> {
        val entityModel: EntityModel<Product> = this.assembler.toModel(this.productService.save(newProduct))
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
            .body<EntityModel<Product>>(entityModel)
    }

    /**
     * Endpoint for retrieving the details of a [Product] with the [PathVariable] ID.
     * @param id The ID of the product to retrieve.
     * @return An [EntityModel] containing the details of the retrieved product.
     */
    @GetMapping("/{id}")
    fun getProduct(@PathVariable id: String): EntityModel<Product> {
        val product = this.productService.getProduct(id)
        return convertToEntityModel(product)
    }

    /**
     * Converts the non-model object [product] to the equivalent
     * model-based object [EntityModel] using the class [assembler].
     * @param product The product object to convert.
     * @return An [EntityModel] containing the details of the converted product.
     */
    private fun convertToEntityModel(product: Product) = this.assembler.toModel(product)

    /**
     * Endpoint for updating an existing [Product] by its [id] with [newProduct].
     *
     * If the [Product] is not found the [newProduct] is saved in the [ProductRepository].
     * @param newProduct The new product to update the existing product's variables.
     * @param id The ID of the product to update.
     * @return An HTTP 201 Created with the updated product as an EntityModel in the body.
     */
    @PutMapping("/{id}")
    fun replaceProduct(@RequestBody newProduct: ProductRepresentation, @PathVariable id: String): ResponseEntity<*> {
        val updatedProduct = this.productService.getProduct(id).also {
            this.productService.updateProduct(it, newProduct)
            this.productService.save(it)
        }

        val entityModel = assembler.toModel(updatedProduct)
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel)
    }

    /**
     * Endpoint for deleting a [Product] with the given [id].
     * @param id The id of the product
     * @return An HTTP 204 No Content response
     */
    @DeleteMapping("/{id}")
    fun deleteProduct(@PathVariable id: String): ResponseEntity<*> {
        this.productService.delete(id)
        return ResponseEntity.noContent().build<Any>()
    }
}