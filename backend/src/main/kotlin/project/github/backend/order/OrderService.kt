package project.github.backend.order

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import project.github.backend.product.ProductNotFoundException
import project.github.backend.product.ProductRepository

/**
 * Service that provides order related operations.
 */
@Service
class OrderService(
    private val productRepository: ProductRepository,
    private val orderRepository: OrderRepository
) {
    /**
     * Creates an order and saves it to the database.
     */
    @Transactional
    fun createOrder(newOrder: OrderController.NewOrder): Order {
        val orderItems = newOrder.items.map { orderItemRequest ->
            val product = productRepository.findById(orderItemRequest.productId)
                .orElseThrow { ProductNotFoundException(orderItemRequest.productId) }
            OrderItem(product = product, quantity = orderItemRequest.quantity)
        }

        val order = Order(orderItems)
        order.setStatus(Status.IN_PROGRESS)

        println("saving order $order")
        return orderRepository.save(order)
    }
}