package project.github.backend

import jakarta.persistence.Entity
import jakarta.persistence.Id

/**
 * This class represents a product as an [Entity].
 * @property id the unique identifier of the product.
 * @property name The name of the product.
 * @property price The price of the product.
 * @property currency The currency in which the price is specified.
 * @property rebateQuantity The quantity required to qualify for a rebate.
 * @property rebatePercent The percentage discount offered as a rebate.
 * @property upsellProduct The ID of the product that is offered as an upsell.
 * @throws IllegalArgumentException If the price, [rebateQuantity] or [rebatePercent] is not valid.
 */
@Entity
class Product(
        @Id private val id: String,
        private var name: String,
        private var price: Int,
        private var currency: String,
        private var rebateQuantity: Int,
        private var rebatePercent: Int,
        private var upsellProduct: String?
) {

    init {
        require(price >= 0) { "Price must be 0 or greater, was $price" }
        require(rebateQuantity >= 0) { "Rebate quantity must be 0 or greater, was $rebateQuantity" }
        require(rebatePercent in 0..100) { "Rebate percent must be between 0 and 100, was $rebatePercent" }
    }

    /**
     * Returns true if the [Product]'s [id]s are equal.
     */
    override fun equals(other: Any?): Boolean {
        if (other !is Product) {
            return false
        }
        return this.id == other.id
    }

    /**
     * Returns a hash code value for the [Product] based solely on the [id].
     */
    override fun hashCode(): Int {
        return id.hashCode()
    }

    /**
     * Builds the toString() return value by each declared field in the class in the format:
     *
     *     Product { id='jeffy-bezzy', name='Jeffrey Bezos', price='3', ... }
     *
     * @return The built string.
     */
    private fun toStringBuilder(): String {
        val className = Product::class.java.name
        val declaredFields = Product::class.java.declaredFields
        val sb = StringBuilder("$className { ")
        val fieldStrings = declaredFields.map { field ->
            "${field.name}='${field.get(this)}'"
        }
        sb.append(fieldStrings.joinToString(", "))
        sb.append(" }")
        return sb.toString()
    }

    /**
     * Returns a string representation of [Product] with all declared fields and their values.
     */
    override fun toString(): String {
        return toStringBuilder()
    }

    fun getName(): String = this.name
    fun getPrice(): Int =this.price
    fun getCurrency(): String = this.currency
    fun getRebateQuantity(): Int = this.rebateQuantity
    fun getRebatePercent(): Int = this.rebatePercent
    fun getUpsellProduct(): String? = this.upsellProduct

    fun setName(name: String) {
        this.name = name
    }
    fun setPrice(price: Int) {
        this.price = price
    }
    fun setCurrency(currency: String) {
        this.currency = currency
    }
    fun setRebateQuantity(rebateQuantity: Int) {
        this.rebateQuantity = rebateQuantity
    }
    fun setRebatePercent(rebatePercent: Int) {
        this.rebatePercent = rebatePercent
    }
    fun setUpsellProduct(upsellProduct: String?) {
        this.upsellProduct = upsellProduct
    }
}