package project.github.backend.order

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.springframework.test.context.junit.jupiter.SpringExtension
import project.github.backend.entity.basket.Basket
import project.github.backend.entity.basket.BasketService
import project.github.backend.entity.order.*
import java.math.BigDecimal
import java.util.*

@ExtendWith(SpringExtension::class)
class OrderServiceTest {
    private val orderRepository = mock(OrderRepository::class.java)
    private val basketService = mock(BasketService::class.java)
    private val orderService = OrderService(orderRepository, basketService)

    private val orderId = 1L
    private val products = mapOf("p1" to 1, "p2" to 2)
    private val basket = Basket(id = 1L, products = emptyList(), numberOfProducts = products.size)
    private val totalPrice = BigDecimal(300)
    private val order = Order(
        id = orderId, basket = basket, totalPrice = totalPrice, status = Status.IN_PROGRESS, currency = "DKK"
    )

    @BeforeEach
    fun setup() {
        `when`(basketService.createBasket(products)).thenReturn(basket)
        `when`(basketService.getTotalPrice(anyLong())).thenReturn(totalPrice)
        `when`(orderRepository.findById(orderId)).thenReturn(Optional.of(order))
        `when`(orderRepository.save(any(Order::class.java))).thenAnswer { invocation -> invocation.arguments[0] }
    }

    @Test
    fun `createOrder returns the created Order given valid payload`() {
        val payload = OrderRepresentation()
        payload.products = products

        val createdOrder = orderService.createOrder(payload)

        assertThat(createdOrder.basket).isEqualTo(basket)
        assertThat(createdOrder.totalPrice).isEqualTo(totalPrice)
        assertThat(createdOrder.status).isEqualTo(Status.IN_PROGRESS)
    }

    @Test
    fun `completeOrder updates the status of the Order to COMPLETED`() {
        val completedOrder = orderService.completeOrder(orderId)

        assertThat(completedOrder.status).isEqualTo(Status.COMPLETED)
    }

    @Test
    fun `cancelOrder updates the status of the Order to CANCELLED`() {
        val cancelledOrder = orderService.cancelOrder(orderId)

        assertThat(cancelledOrder.status).isEqualTo(Status.CANCELLED)
    }

    @Test
    fun `getOrder returns the Order given a valid id`() {
        val foundOrder = orderService.getOrder(orderId)

        assertThat(foundOrder).isEqualTo(order)
    }
}
