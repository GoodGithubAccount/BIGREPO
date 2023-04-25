package project.github.backend.order

import com.fasterxml.jackson.annotation.*
import jakarta.persistence.*
import project.github.backend.product.Product
import java.lang.IllegalStateException
import java.util.*
import kotlin.collections.ArrayList


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
class Order private constructor(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null,
    private var status: Status = Status.IN_PROGRESS,
    @OneToMany(
        mappedBy = "order",
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
        fetch = FetchType.EAGER
    ) @JsonManagedReference private val _orderItems: MutableList<OrderItem> = mutableListOf()
) {
    /**
     * TODO Should not be used :)
     */
    constructor() : this(null, Status.IN_PROGRESS)

    val orderItems: List<OrderItem>
        get() = ArrayList(_orderItems)

    constructor(orderItems: List<OrderItem>) : this() {
        _orderItems.addAll(orderItems)
        orderItems.forEach { it.order = this }
    }

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

    fun getId(): Long = this.id ?: throw IllegalStateException("ID has not been set yet")
    fun getStatus(): Status = this.status
    fun setStatus(status: Status) {
        this.status = status
    }
}

@Entity
class OrderItem(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @JsonProperty("id") val id: Long? = null,
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "order_id") @JsonBackReference var order: Order? = null,
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "product_id") val product: Product,
    val quantity: Int
) {

    /**
     * Returns true if the [OrderItem]'s [id]s are equal and the quantities are equal.
     */
    override fun equals(other: Any?): Boolean {
        if (other !is OrderItem) {
            return false
        }
        return this.id == other.id && this.quantity == other.quantity
    }

    /**
     * Returns a hash code value for the [OrderItem] based solely on the [id].
     */
    override fun hashCode(): Int {
        return id.hashCode()
    }

    private fun toStringBuilder(): String {
        val className = OrderItem::class.java.name
        val declaredFields = OrderItem::class.java.declaredFields
        val sb = StringBuilder("$className { ")
        val fieldStrings = declaredFields.mapNotNull { field ->
            if (field.name != "order") {
                "${field.name}='${field.get(this)}'"
            } else {
                null
            }
        }
        sb.append(fieldStrings.joinToString(", "))
        sb.append(" }")
        return sb.toString()
    }

    /**
     * Returns a string representation of [OrderItem] with all declared fields and their values.
     */
    override fun toString(): String {
        return toStringBuilder()
    }
}