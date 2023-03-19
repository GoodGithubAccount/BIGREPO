package project.github.backend.product

import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.RepresentationModelAssembler
import org.springframework.hateoas.server.core.DummyInvocationUtils.methodOn
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.stereotype.Component

/**
 * A [RepresentationModelAssembler] implementation that converts the domain type [Product] object
 * into its [EntityModel] representation.
 *
 * The class is marked as [Component] and will automatically be created on startup.
 */
@Component
class ProductModelAssembler : RepresentationModelAssembler<Product, EntityModel<Product>> {

    /**
     * Converts the non-model object [product] to the equivalent model-based object
     * [EntityModel] of the [product] including the self link and link to all products.
     *
     * @param product the non-model object to be converted.
     * @return the converted model-based object.
     */
    override fun toModel(product: Product): EntityModel<Product> {
        return EntityModel.of(product,
                linkTo(methodOn(ProductController::class.java).getProduct(product.getId())).withSelfRel(),
                linkTo(methodOn(ProductController::class.java).all()).withRel("products")
        )
    }
}