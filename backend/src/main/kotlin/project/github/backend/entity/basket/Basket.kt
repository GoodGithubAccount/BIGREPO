package project.github.backend.entity.basket

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import project.github.backend.entity.basketproduct.BasketProduct

@Entity
data class Basket(
    @Id @GeneratedValue @JsonIgnore val id: Long? = null,
    @OneToMany(cascade = [CascadeType.ALL]) val products: List<BasketProduct>, //TODO add HREF link
    val numberOfProducts: Int,
)