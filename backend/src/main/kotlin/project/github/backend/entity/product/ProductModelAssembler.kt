package project.github.backend.entity.product

import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.RepresentationModelAssembler
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
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
     * [EntityModel] of the [product] including the self link, link to all products and if present an upsellProduct [Product].
     *
     * @param product the non-model object to be converted.
     * @return the converted model-based object.
     */
    override fun toModel(product: Product): EntityModel<Product> {
        val model = EntityModel.of(product)
        model.addSelfRel()

        if (product.upsellProduct != null) {
            model.add(linkTo(methodOn(ProductController::class.java).getProduct(product.upsellProduct!!)).withRel("upsellProduct"))
        }

        return model
    }

    private fun EntityModel<Product>.addSelfRel() {
        val productId = this.content!!.id

        val controllerClass = ProductController::class.java

        add(linkTo(methodOn(controllerClass).getProduct(productId)).withSelfRel())
    }
}