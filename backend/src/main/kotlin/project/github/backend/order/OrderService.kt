package project.github.backend.order

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import project.github.backend.basket.Basket
import project.github.backend.basket.BasketService
import project.github.backend.order.exceptions.IllegalOrderCancellationException
import project.github.backend.order.exceptions.IllegalOrderCompletionException
import project.github.backend.order.exceptions.OrderNotFoundException

/**
 * Service that provides [Order] related operations with the database.
 */
@Service
class OrderService(
    private val orderRepository: OrderRepository, private val basketService: BasketService
) {
    /**
     * Creates an [Order] in the database.
     * @param payload the [OrderRepresentation] containing the [Basket] products.
     */
    @Transactional
    fun createOrder(payload: OrderRepresentation): Order {
        val basketProducts = payload.products!!

        val basket = basketService.createBasket(basketProducts)
        val order = constructOrder(basket)

        return orderRepository.save(order)
    }

    private fun constructOrder(basket: Basket): Order {
        return Order(
            basket = basket, totalPrice = basketService.getTotalPrice(basket.id!!), status = Status.IN_PROGRESS
        )
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

    /**
     * Fetches an [Order] from the database.
     * @param id the id of the [Order] to be fetched.
     * @return the fetched [Order].
     * @throws OrderNotFoundException if the [Order] is not found.
     */
    fun getOrder(id: Long): Order {
        return orderRepository.findById(id).orElseThrow { OrderNotFoundException(id) }
    }

    /**
     * Fetches all [Order]s from the database.
     * @return the fetched [Order]s.
     */
    fun getAllOrders(): List<Order> {
        return orderRepository.findAll()
    }
}