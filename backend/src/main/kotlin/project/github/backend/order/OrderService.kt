package project.github.backend.order

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import project.github.backend.order.exceptions.IllegalOrderCancellationException
import project.github.backend.order.exceptions.IllegalOrderCompletionException
import project.github.backend.order.exceptions.IllegalDuplicateProductException
import project.github.backend.order.exceptions.OrderNotFoundException
import project.github.backend.product.ProductNotFoundException
import project.github.backend.product.ProductRepository
import project.github.backend.product.Product

/**
 * Service that provides order related operations.
 */
@Service
class OrderService(
    private val productRepository: ProductRepository,
    private val orderRepository: OrderRepository
) {
    /**
     * Creates an [Order] and saves it to the database.
     * @param newOrder the list of items with their quantities to be ordered.
     * @throws IllegalDuplicateProductException if the same [Product] is ordered twice.
     * @throws ProductNotFoundException if a product in the [OrderService.NewOrder] is not found.
     */
    @Transactional
    fun createOrder(newOrder: NewOrder): Order {
        val productIds = mutableSetOf<String>()

        val orderItems = newOrder.items.map { orderItemRequest ->
            val productId = orderItemRequest.productId

            if (productIds.contains(productId)) {
                throw IllegalDuplicateProductException(productId)
            }

            val product = productRepository.findById(productId)
                .orElseThrow { ProductNotFoundException(productId) }

            productIds.add(productId)

            OrderItem(product = product, quantity = orderItemRequest.quantity)
        }

        val order = Order(orderItems = orderItems)
        order.setStatus(Status.IN_PROGRESS)

        println("saving order $order")
        return orderRepository.save(order)
    }

    data class OrderItemRequest(
        val productId: String, val quantity: Int
    )

    data class NewOrder(
        val items: List<OrderItemRequest>
    )

    /**
     * Returns an [Order] from the database by its id.
     * @param id the id of the [Order].
     * @throws OrderNotFoundException if the [Order] is not found.
     */
    fun getOrder(id: Long): Order =
        orderRepository.findById(id).orElseThrow { OrderNotFoundException(id) }

    /**
     * Returns all [Order]s from the database.
     */
    fun getAllOrders(): List<Order> {
        return orderRepository.findAll()
    }

    /**
     * Completes an [Order] in the database.
     * @param id the id of the [Order] to be completed.
     * @throws IllegalOrderCompletionException if the [Order] is already completed or cancelled.
     */
    fun completeOrder(id: Long): Order {
        val order = getOrder(id)

        if (order.getStatus() == Status.COMPLETED || order.getStatus() == Status.CANCELLED) {
            throw IllegalOrderCompletionException(id, order.getStatus())
        }
        order.setStatus(Status.COMPLETED)
        return order
    }

    /**
     * Cancels an [Order] in the database.
     * @param id the id of the [Order] to be cancelled.
     * @throws IllegalOrderCancellationException if the [Order] is already cancelled or completed.
     */
    fun cancelOrder(id: Long): Order {
        val order = getOrder(id)

        if (order.getStatus() == Status.CANCELLED || order.getStatus() == Status.COMPLETED) {
            throw IllegalOrderCancellationException(id, order.getStatus())
        }
        order.setStatus(Status.CANCELLED)
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
}