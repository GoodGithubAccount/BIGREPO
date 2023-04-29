package project.github.backend.entity.product

import org.springframework.hateoas.RepresentationModel
import java.math.BigDecimal

/**
 * The [RepresentationModel] of a [Product].
 * It is the form in which the client uses to create a product.
 */
class ProductRepresentation : RepresentationModel<ProductRepresentation>() {
    val id: String? = null
    val name: String? =  null
    val price: BigDecimal? = null
    val currency: String? = null //TODO currency verification https://docs.spring.io/spring-hateoas/docs/current/reference/html/#mediatypes.hal-forms.options
    val rebateQuantity: Int? = null
    val rebatePercent: BigDecimal? = null
    val upsellProductId: String? = null //TODO runtime verification?
}