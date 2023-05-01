package project.github.backend.entity.basket.basketproduct

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface BasketProductRepository : JpaRepository<BasketProduct, Long> {
    fun findByBasketIdAndId(basketId: Long, basketProductId: Long): Optional<BasketProduct>
}