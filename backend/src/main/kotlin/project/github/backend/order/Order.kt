package project.github.backend.order

import com.fasterxml.jackson.annotation.*
import jakarta.persistence.*
import project.github.backend.basket.Basket
import java.math.BigDecimal

/**
 * The enum class representing the status of an [Order].
 * @property IN_PROGRESS The order is in progress.
 * @property COMPLETED The order is completed.
 * @property CANCELLED The order is cancelled.
 */
enum class Status {
    IN_PROGRESS, COMPLETED, CANCELLED
}

@Entity
@Table(name = "CUSTOMER_ORDER")
data class Order(
    @Id @GeneratedValue @JsonIgnore val id: Long? = null,
    @OneToOne(cascade = [CascadeType.ALL]) @JsonIgnore val basket: Basket, //TODO add HREF link
    val totalPrice: BigDecimal,
    var status: Status,
)