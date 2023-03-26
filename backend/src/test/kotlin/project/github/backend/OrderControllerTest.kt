package project.github.backend

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.*
import org.springframework.http.*
import org.springframework.test.annotation.DirtiesContext
import project.github.backend.order.Order
import project.github.backend.order.OrderRepository
import project.github.backend.order.Status

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = ["spring.datasource.url=jdbc:h2:mem:testdb"]
)
class OrderControllerTest(
    @Autowired val client: TestRestTemplate,
    @Autowired val orderRepository: OrderRepository,
) {

    @DirtiesContext
    @Test
    fun `getting all orders returns all products`() {
        val order1 = Order(emptyList())
        val order2 = Order(emptyList())
        orderRepository.save(order1)
        orderRepository.save(order2)

        val entity = getEntityForAllOrders()

        val order1Id = order1.getId().toString()
        val order2Id = order2.getId().toString()
        //TODO use better method for determining if product is present
        assertThat(entity.body).contains(order1Id).contains(order2Id)
    }

    @Test
    fun `getting all orders returns 200 OK`() {
        val entity = getEntityForAllOrders()
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
    }

    @DirtiesContext
    @Test
    fun `cancelling an already cancelled order returns 405 method not allowed`() {
        val order = Order(emptyList())
        order.setStatus(Status.CANCELLED)
        orderRepository.save(order)

        val invalidCancelMethod = deleteCancelOrderForEntity(order)

        assertThat(invalidCancelMethod.statusCode).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED)
    }

    @DirtiesContext
    @Test
    fun `cancelling an already completed order returns 405 method not allowed`() {
        val order = Order(emptyList())
        order.setStatus(Status.COMPLETED)
        orderRepository.save(order)

        val invalidCompleteMethod = deleteCancelOrderForEntity(order)

        assertThat(invalidCompleteMethod.statusCode).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED)
    }

    @DirtiesContext
    @Test
    fun `completing an already cancelled order returns 405 method not allowed`() {
        val order = Order(emptyList())
        order.setStatus(Status.CANCELLED)
        orderRepository.save(order)

        val invalidCompleteMethod = putCompleteOrderForEntity(order)

        assertThat(invalidCompleteMethod.statusCode).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED)
    }

    @DirtiesContext
    @Test
    fun `completing an already completed order returns 405 method not allowed`() {
        val order = Order(emptyList())
        order.setStatus(Status.COMPLETED)
        orderRepository.save(order)

        val invalidCompleteMethod = putCompleteOrderForEntity(order)

        assertThat(invalidCompleteMethod.statusCode).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED)
    }

    @DirtiesContext
    @Test
    fun `cancelling an order sets status to cancelled`() {
        val order = Order(emptyList())

        orderRepository.save(order)
        deleteCancelOrderForEntity(order)

        val orderId = order.getId()
        val cancelledOrder = orderRepository.findById(orderId).get()
        assertThat(cancelledOrder.getStatus()).isEqualTo(Status.CANCELLED)
    }

    @DirtiesContext
    @Test
    fun `cancelling an order returns 200 OK`() {
        val order = Order(emptyList())

        orderRepository.save(order)
        val response = deleteCancelOrderForEntity(order)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    @DirtiesContext
    @Test
    fun `completing an order sets status to completed`() {
        val order = Order(emptyList())

        orderRepository.save(order)
        putCompleteOrderForEntity(order)

        val orderId = order.getId()
        val completedOrder = orderRepository.findById(orderId).get()
        assertThat(completedOrder.getStatus()).isEqualTo(Status.COMPLETED)
    }

    @DirtiesContext
    @Test
    fun `completing an order returns 200 OK`() {
        val order = Order(emptyList())

        orderRepository.save(order)
        val response = putCompleteOrderForEntity(order)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    private fun getEntityForAllOrders() = client.getForEntity<String>("/orders")
    private fun putCompleteOrderForEntity(order: Order) =
        client.exchange("/orders/{id}/complete", HttpMethod.PUT, HttpEntity(order), Order::class.java, order.getId())
    private fun deleteCancelOrderForEntity(order: Order) =
        client.exchange("/orders/{id}/cancel", HttpMethod.DELETE, HttpEntity(order), Order::class.java, order.getId())
}