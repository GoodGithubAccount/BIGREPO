package project.github.backend.order

import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.Matchers.endsWith
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.web.client.*
import org.springframework.hateoas.EntityModel
import org.springframework.http.MediaType
import org.springframework.http.RequestEntity.*
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import project.github.backend.entity.basket.Basket
import project.github.backend.entity.basketproduct.BasketProduct
import project.github.backend.entity.order.*
import java.math.BigDecimal

@WebMvcTest(OrderController::class)
class OrderControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var orderService: OrderService

    @MockBean
    private lateinit var assembler: OrderModelAssembler


    private val currency = "DKK"
    private val basketProduct1 =
        BasketProduct(productId = "p1", quantity = 1, price = BigDecimal(100), currency = currency)
    private val basketProduct2 =
        BasketProduct(productId = "p2", quantity = 2, price = BigDecimal(200), currency = currency)
    private val basketProducts = listOf(basketProduct1, basketProduct2)
    private val numberOfProducts = basketProducts.sumOf { it.quantity }
    private val basket = Basket(products = basketProducts, numberOfProducts = numberOfProducts)

    private val orderId = 1L
    private val totalPrice = BigDecimal(500)
    private val order = Order(id = orderId, basket = basket, totalPrice = totalPrice, currency = currency)

    private val orderRepresentation = OrderRepresentation().also { it.products = mapOf("p1" to 1, "p2" to 2) }
    private val orderModel = EntityModel.of(order)

    @BeforeEach
    fun setup() {
        `when`(orderService.createOrder(orderRepresentation)).thenReturn(order)
        `when`(orderService.getOrder(orderId)).thenReturn(order)
        `when`(orderService.getAllOrders()).thenReturn(listOf(order))
        `when`(orderService.completeOrder(orderId)).thenReturn(order.copy(status = Status.COMPLETED))
        `when`(orderService.cancelOrder(orderId)).thenReturn(order.copy(status = Status.CANCELLED))
        `when`(assembler.toModel(order)).thenReturn(orderModel)
    }

    @Test
    fun `orders returns a list of orders`() {
        // Prepare expected data
        val orders = listOf(order)

        // Test
        mockMvc.perform(get("/orders"))
            .andExpect(status().isOk)
            .andExpect(content().contentType("application/hal+json"))
            .andExpect(jsonPath("$._links.self.href", endsWith("/orders")))
            .andExpect(jsonPath("$._embedded.orderList[0].totalPrice").value(totalPrice.toPlainString()))
            .andExpect(jsonPath("$._embedded.orderList[0].status").value("IN_PROGRESS"))
            .andExpect(jsonPath("$._embedded.orderList[0].currency").value(currency))
    }

    @Test
    fun `createOrder returns the created order`() {
        mockMvc.perform(
            post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(orderRepresentation))
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType("application/hal+json"))
            .andExpect(jsonPath("$.totalPrice").value(totalPrice.toPlainString()))
            .andExpect(jsonPath("$.status").value("IN_PROGRESS"))
            .andExpect(jsonPath("$.currency").value(currency))
    }

    @Test
    fun `getOrder returns the order given a valid id`() {
        mockMvc.perform(get("/orders/$orderId"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.totalPrice").value(totalPrice.toPlainString()))
            .andExpect(jsonPath("$.status").value("IN_PROGRESS"))
            .andExpect(jsonPath("$.currency").value(currency))
    }

    //FIXME returns empty body for some reason
    @Test
    fun `complete updates the status of the order to COMPLETED`() {
        mockMvc.perform(put("/orders/$orderId/complete"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.status").value("COMPLETED"))
    }

    //FIXME returns empty body for some reason
    @Test
    fun `cancel updates the status of the order to CANCELLED`() {
        mockMvc.perform(delete("/orders/$orderId/cancel"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.status").value("CANCELLED"))
    }
}