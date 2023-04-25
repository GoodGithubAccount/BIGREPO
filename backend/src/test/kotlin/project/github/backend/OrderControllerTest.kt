package project.github.backend

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.*
import org.springframework.http.*
import org.springframework.test.annotation.DirtiesContext
import project.github.backend.order.Order
import project.github.backend.order.OrderController
import project.github.backend.order.OrderRepository
import project.github.backend.order.Status
import project.github.backend.product.Product
import project.github.backend.product.ProductRepository
import javax.net.ssl.HttpsURLConnection

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = [
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.config.location=classpath:application-test.properties",
    ]
)
class OrderControllerTest(
    @Autowired val client: TestRestTemplate,
    @Autowired val orderRepository: OrderRepository,
    @Autowired val productRepository: ProductRepository
) {

    @BeforeEach
    fun setup() {
        HttpsURLConnection.setDefaultHostnameVerifier { hostname, _ ->
            hostname == "localhost"
        }
    }

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
    
    @Test
    fun `getting an invalid order ID returns 400 BAD REQUEST`() {
        val invalidId = "a"
        val response = getEntityForId(invalidId)

        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    fun `getting an non-existing order ID returns 404 NOT FOUND`() {
        val nonExistingId = "0"
        val response = getEntityForId(nonExistingId)

        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }

    @Test
    fun `posting an order returns 201 CREATED`() {
        val p1 = Product("p1", "p1", 0, "DKK", 0, 0, null)
        val p2 = Product("p2", "p2", 0, "USD", 0, 0, "p1")

        productRepository.save(p1)
        productRepository.save(p2)

        val newOrder = OrderController.NewOrder(
            listOf(
                OrderController.OrderItemRequest("p1", 2), OrderController.OrderItemRequest("p2", 1)
            )
        )

        val response = postOrderForEntity(newOrder)

        assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)
        println(response.body)
    }

    @Test
    fun `posting an order saves the order`() {
        val p1 = Product("p1", "p1", 0, "DKK", 0, 0, null)
        productRepository.save(p1)
        val newOrder = OrderController.NewOrder(
            listOf(
                OrderController.OrderItemRequest("p1", 2)
            )
        )

        val createdOrder = postOrderForObject(newOrder)!!
        val order = orderRepository.findById(createdOrder.getId())

        assertThat(order).isPresent
        assertThat(order.get().orderItems.first().product).isEqualTo(p1)
    }

    @Test
    fun `posting an order with invalid product returns 404 NOT FOUND`() {
        val newOrder = OrderController.NewOrder(
            listOf(
                OrderController.OrderItemRequest("p1", 2), OrderController.OrderItemRequest("p2", 1)
            )
        )

        val response = postOrderForEntity(newOrder)

        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        assertThat(response.body.toString()).contains("p1")
    }

    private fun postOrderForEntity(order: OrderController.NewOrder) =
        client.postForEntity<String>("/orders", HttpEntity(order))

    private fun postOrderForObject(order: OrderController.NewOrder) =
        client.postForObject<Order>("/orders", HttpEntity(order))

    private fun getEntityForId(id: String) = client.getForEntity<String>("/orders/$id")
    private fun getEntityForAllOrders() = client.getForEntity<String>("/orders")
    private fun putCompleteOrderForEntity(order: Order) =
        client.exchange<String>("/orders/{id}/complete", HttpMethod.PUT, HttpEntity(order), order.getId())
    private fun deleteCancelOrderForEntity(order: Order) =
        client.exchange<String>("/orders/{id}/cancel", HttpMethod.DELETE, HttpEntity(order), order.getId())
}