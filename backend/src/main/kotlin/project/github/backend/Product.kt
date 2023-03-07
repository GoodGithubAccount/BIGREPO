package project.github.backend

import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
class Product(@Id val id: String, private var name: String, private var price: Int, private var currency: String, private var rebateQuantity: Int, private var rebatePercent: Int, private var upsellProduct: String?) {

    constructor() : this("default-id", "default-name", 0, "DKK", 0, 0, null)

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

    override fun toString(): String {
        return "Product { id='${this.id}', name='${this.name}', price='${this.price}', currency='${this.currency}', " +
               "rebateQuantity='${this.rebateQuantity}', rebatePercent='${this.rebatePercent}', upsellProduct='${this.upsellProduct}' }"
    }
}