package project.github.backend.entity.basket.basketproduct

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import project.github.backend.entity.basket.Basket
import java.math.BigDecimal

@Entity
data class BasketProduct(
    @Id @GeneratedValue @JsonIgnore val id: Long? = null,
    val productId: String,
    val quantity: Int,
    val price: BigDecimal,
    val currency: String,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "basket_id")
    @JsonIgnore val basket: Basket
) {
    override fun toString(): String {
        return "BasketProduct(id=$id, productId='$productId', quantity=$quantity, price=$price, currency='$currency', basketId=${basket.id})"
    }
}