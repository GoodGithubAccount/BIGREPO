package project.github.backend.order

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import project.github.backend.order.exceptions.IllegalDuplicateProductException
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
}