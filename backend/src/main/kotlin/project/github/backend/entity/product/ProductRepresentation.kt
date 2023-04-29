package project.github.backend.entity.product

import org.springframework.hateoas.RepresentationModel
import java.math.BigDecimal

/**
 * The [RepresentationModel] of a [Product].
 * It is the form in which the client uses to create a product.
 */
class ProductRepresentation : RepresentationModel<ProductRepresentation>() {
    var id: String? = null
    var name: String? =  null
    var price: BigDecimal? = null
    var currency: String? = null //TODO currency verification https://docs.spring.io/spring-hateoas/docs/current/reference/html/#mediatypes.hal-forms.options
    var rebateQuantity: Int? = null
    var rebatePercent: BigDecimal? = null
    var upsellProductId: String? = null //TODO runtime verification?
}