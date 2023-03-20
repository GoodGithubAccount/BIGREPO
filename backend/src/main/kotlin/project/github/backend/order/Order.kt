package project.github.backend.order

import jakarta.persistence.*
import project.github.backend.product.Product


enum class Status {
    IN_PROGRESS, COMPLETED, CANCELLED
}

/**
 * This class represents an order as an [Entity].
 * @property id The unique identifier of the order.
 * @property status The status of the order.
 * @property products A list of [Product]s in the order.
 */
@Entity
@Table(name = "CUSTOMER_ORDER")
class Order(
    private var status: Status,
    @ManyToMany private var products: List<Product>
) {

    @Id
    @GeneratedValue
    private var id: Long? = null

    /**
     * Returns true if the [Order]'s [id]s are equal.
     */
    override fun equals(other: Any?): Boolean {
        if (other !is Order) {
            return false
        }
        return this.id == other.id
    }

    /**
     * Returns a hash code value for the [Order] based solely on the [id].
     */
    override fun hashCode(): Int {
        return id.hashCode()
    }

    private fun toStringBuilder(): String {
        val className = Order::class.java.name
        val declaredFields = Order::class.java.declaredFields
        val sb = StringBuilder("$className { ")
        val fieldStrings = declaredFields.map { field ->
            "${field.name}='${field.get(this)}'"
        }
        sb.append(fieldStrings.joinToString(", "))
        sb.append(" }")
        return sb.toString()
    }

    /**
     * Returns a string representation of [Order] with all declared fields and their values.
     */
    override fun toString(): String {
        return toStringBuilder()
    }

    fun getId(): Long = this.id!!
    fun getStatus(): Status = this.status
    fun getProducts(): List<Product> = this.products

    fun setStatus(status: Status) {
        this.status = status
    }
}