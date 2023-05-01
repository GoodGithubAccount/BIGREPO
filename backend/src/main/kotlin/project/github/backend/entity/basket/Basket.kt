package project.github.backend.entity.basket

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import project.github.backend.entity.basket.basketproduct.BasketProduct
import project.github.backend.entity.order.Order

@Entity
data class Basket(
    @Id @GeneratedValue @JsonIgnore val id: Long? = null,
    @OneToMany(mappedBy = "basket", cascade = [CascadeType.ALL]) var products: List<BasketProduct>, //TODO add HREF link
    var numberOfProducts: Int,
    @OneToOne(mappedBy = "basket", fetch = FetchType.LAZY) @JsonIgnore var order: Order? = null
) {
    override fun toString(): String {
        return "Basket(id=$id, products=$products, numberOfProducts=$numberOfProducts, orderId=${order?.id})"
    }
}