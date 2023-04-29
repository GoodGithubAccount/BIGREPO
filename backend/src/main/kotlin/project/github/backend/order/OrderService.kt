package project.github.backend.order

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import project.github.backend.basket.Basket
import project.github.backend.basketproduct.BasketProduct
import project.github.backend.order.exceptions.IllegalOrderCancellationException
import project.github.backend.order.exceptions.IllegalOrderCompletionException
import project.github.backend.order.exceptions.IllegalDuplicateProductException
import project.github.backend.order.exceptions.OrderNotFoundException
import project.github.backend.product.ProductNotFoundException
import project.github.backend.product.ProductRepository
import java.math.BigDecimal

/**
 * Service that provides order related operations.
 */
@Service
class OrderService(
    private val productRepository: ProductRepository, private val orderRepository: OrderRepository
) {
    /**
     * TODO KDoc
     */
    @Transactional
    fun createOrder(payload: OrderRepresentation): Order {
        val basketProducts = constructBasketProducts(payload)

        val basket = Basket(
            products = basketProducts, numberOfProducts = basketProducts.count(), currency = payload.currency!!
        )
        val order = constructOrder(basket, basketProducts)

        return orderRepository.save(order)
    }

    private fun constructOrder(basket: Basket, basketProducts: List<BasketProduct>): Order {
        return Order(
            basket = basket,
            totalPrice = getTotalPrice(basketProducts),
            currency = basket.currency,
            status = Status.IN_PROGRESS
        )
    }

    private fun getTotalPrice(basketProducts: List<BasketProduct>): BigDecimal {
        return basketProducts.stream().map { basketProduct ->
            basketProduct.price * basketProduct.quantity.toBigDecimal()
        }.reduce { a, b -> a + b }.get()
    }

    private fun constructBasketProducts(payload: OrderRepresentation): List<BasketProduct> {
        val productIds = mutableSetOf<String>()

        return payload.products!!.map { product ->
            val id = product.key
            val quantity = product.value

            val foundProduct = productRepository.findById(id).orElseThrow { ProductNotFoundException(id) }

            if (productIds.contains(id)) {
                throw IllegalDuplicateProductException(id)
            }

            productIds.add(id)

            BasketProduct(
                productId = id,
                quantity = quantity,
                price = foundProduct.price.toBigDecimal(),
            )
        }
    }

    /**
     * Completes an [Order] in the database.
     * @param id the id of the [Order] to be completed.
     * @throws IllegalOrderCompletionException if the [Order] is already completed or cancelled.
     */
    fun completeOrder(id: Long): Order {
        val order = getOrder(id)

        if (order.status == Status.COMPLETED || order.status == Status.CANCELLED) {
            throw IllegalOrderCompletionException(id, order.status)
        }
        order.status = Status.COMPLETED
        return order
    }

    /**
     * Cancels an [Order] in the database.
     * @param id the id of the [Order] to be cancelled.
     * @throws IllegalOrderCancellationException if the [Order] is already cancelled or completed.
     */
    fun cancelOrder(id: Long): Order {
        val order = getOrder(id)

        if (order.status == Status.CANCELLED || order.status == Status.COMPLETED) {
            throw IllegalOrderCancellationException(id, order.status)
        }
        order.status = Status.CANCELLED
        return order
    }

    /**
     * Saves an [Order] to the database.
     * @param order the [Order] to be saved, must not be null.
     * @return the saved [Order].
     */
    fun save(order: Order): Order {
        return orderRepository.save(order)
    }

    fun getOrder(id: Long): Order {
        return orderRepository.findById(id).orElseThrow { OrderNotFoundException(id) }
    }

    fun getAllOrders(): List<Order> {
        return orderRepository.findAll()
    }
}