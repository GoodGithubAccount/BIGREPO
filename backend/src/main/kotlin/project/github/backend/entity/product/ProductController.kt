package project.github.backend.entity.product

import project.github.backend.entity.product.exception.IllegalProductOperationException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.hateoas.*
import org.springframework.hateoas.mediatype.hal.HalModelBuilder
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*
import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import project.github.backend.entity.product.exception.ProductNotFoundException

/**
 * This class represents the [RestController] for handling HTTP requests related to [Product] entities.
 * It defines several endpoints for managing CRUD operations of the [Product] entities.
 * @param productAssembler The [ProductModelAssembler] instance used to convert [Product] entities to [EntityModel]s.
 * @param productService The [ProductService] instance used to perform CRUD operations on [Product] entities.
 */
@RestController
@RequestMapping("/products")
class ProductController(
    private val productAssembler: ProductModelAssembler,
    private val productService: ProductService
) {
    private val log: Logger = LoggerFactory.getLogger(ProductController::class.java)

    /**
     * Endpoint for Retrieving all [Product]s from the database and returns them as a collection of [EntityModel]s.
     * @return Links to all existing products
     */
    @GetMapping()
    fun all(): HttpEntity<*> {
        val allProducts = this.productService.getAllProducts()

        val selfLink = linkTo(methodOn(ProductController::class.java).all()).withSelfRel()
            .andAffordance(afford(methodOn(ProductController::class.java).newProduct(null)))
        //https://github.com/spring-projects/spring-hateoas/issues/1186
        val findLink = linkTo(methodOn(ProductController::class.java).getProduct(null)).withRel("find")

        val productLinks = allProducts.map { product ->
            linkTo(methodOn(ProductController::class.java).getProduct(product.id)).withRel("product")
                .withTitle(product.name)
        }

        val model = HalModelBuilder.emptyHalModel()
                .link(selfLink)
                .link(findLink)
                .links(productLinks)
                .build()

        return ResponseEntity.ok(model)
    }

    /**
     * Endpoint for saving a given [Product] instance to the [ProductRepository].
     * @param newProduct The [Product] entity to be saved.
     * @return The saved product
     */
    @PostMapping()
    fun newProduct(@RequestBody newProduct: ProductRepresentation?): HttpEntity<*> {
        if (newProduct == null) {
            throw IllegalProductOperationException("Product representation cannot be null")
        }

        val savedProductId = this.productService.save(newProduct).id

        return getProduct(savedProductId)
    }

    /**
     * Endpoint for retrieving the details of a [Product] with the [PathVariable] ID.
     * @param id The ID of the product to retrieve.
     * @return An [EntityModel] containing the details of the retrieved product.
     */
    @GetMapping("/{id}")
    fun getProduct(@PathVariable id: String?): HttpEntity<*> {
        //https://github.com/spring-projects/spring-hateoas/issues/1186
        if (id == null) {
            throw IllegalProductOperationException("Product id cannot be null")
        }
        val product = this.productService.getProduct(id)

        val productControllerProxy = methodOn(ProductController::class.java)

        val updateTemplate = afford(productControllerProxy.replaceProduct(null, null))
        val deleteTemplate = afford(productControllerProxy.deleteProduct(null))

        val templates = listOf(updateTemplate, deleteTemplate)

        val selfLink = linkTo(productControllerProxy.getProduct(id)).withSelfRel()
            .andAffordances(templates)
        val allLink = linkTo(productControllerProxy.all()).withRel("all")
        val findLink = linkTo(methodOn(ProductController::class.java).getProduct(null)).withRel("find")

        val links = listOf(selfLink, allLink, findLink)

        val model = HalModelBuilder.halModel()
            .embed(product)
            .links(links)
            .build()

        return ResponseEntity.ok(model)
    }

    /**
     * Endpoint for updating an existing [Product] by its [id] with [newProduct].
     *
     * If the [Product] is not found the [newProduct] is saved in the [ProductRepository].
     * @param newProduct The new product to update the existing product's variables.
     * @param id The ID of the product to update.
     * @return An HTTP 201 Created with the updated product as an EntityModel in the body.
     */
    @PutMapping("/{id}")
    fun replaceProduct(@RequestBody newProduct: ProductRepresentation?, @PathVariable id: String?): HttpEntity<*> {
        //https://github.com/spring-projects/spring-hateoas/issues/1186
        if (newProduct == null) {
            throw IllegalProductOperationException("Product Representation cannot be null")
        }

        //https://github.com/spring-projects/spring-hateoas/issues/1186
        if (id == null) {
            throw ProductNotFoundException("Product id cannot be null")
        }

        val updatedProduct: Product = this.productService.updateProduct(id, newProduct)
        val savedProductId = this.productService.save(updatedProduct).id

        return getProduct(savedProductId)
    }

    /**
     * Endpoint for deleting a [Product] with the given [id].
     * @param id The id of the product
     * @return An HTTP 204 No Content response
     */
    @DeleteMapping("/{id}")
    fun deleteProduct(@PathVariable id: String?): ResponseEntity<*> {
        if (id == null) {
            throw ProductNotFoundException("Product id cannot be null")
        }
        this.productService.delete(id)
        return ResponseEntity.noContent().build<Any>()
    }
}