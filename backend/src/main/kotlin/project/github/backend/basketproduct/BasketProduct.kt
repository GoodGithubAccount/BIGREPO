package project.github.backend.basketproduct

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.math.BigDecimal

@Entity
data class BasketProduct(
    @Id @GeneratedValue @JsonIgnore val id: Long? = null,
    val productId: String,
    val quantity: Int,
    val price: BigDecimal,
)