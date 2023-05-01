package project.github.backend.basket

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.*
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.springframework.test.context.junit.jupiter.SpringExtension
import project.github.backend.entity.basket.Basket
import project.github.backend.entity.basket.BasketRepository
import project.github.backend.entity.basket.BasketService
import project.github.backend.entity.basketproduct.BasketProduct
import project.github.backend.entity.basketproduct.BasketProductService
import java.math.BigDecimal
import java.util.*

@ExtendWith(SpringExtension::class)
class BasketServiceTest {
    private val basketRepository = mock(BasketRepository::class.java)
    private val basketProductService = mock(BasketProductService::class.java)
    private val basketService = BasketService(basketRepository, basketProductService)

    private val basketId = 1L
    private val products = mapOf("p1" to 1, "p2" to 2)
    private val basketProducts = products.map { (id, quantity) ->
        BasketProduct(productId = id, quantity = quantity, price = BigDecimal(100), currency = "DKK")
    }
    private val basket = Basket(id = basketId, products = basketProducts, numberOfProducts = basketProducts.size)

    @BeforeEach
    fun setup() {
        //mock repository and service behaviors
        `when`(basketRepository.findById(basketId)).thenReturn(Optional.of(basket))
        `when`(basketProductService.createBasketProduct(anyString(), anyInt())).thenAnswer { invocation ->
            val id = invocation.arguments[0] as String
            val quantity = invocation.arguments[1] as Int
            BasketProduct(productId = id, quantity = quantity, price = BigDecimal(100), currency = "DKK")
        }
        `when`(basketRepository.save(any(Basket::class.java))).thenAnswer { invocation -> invocation.arguments[0] }
    }

    @Test
    fun `getBasket returns the Basket given a valid id`() {
        val foundBasket = basketService.getBasket(basketId)

        assertThat(foundBasket).isEqualTo(basket)
    }

    @Test
    fun `createBasket returns the created Basket given valid products`() {
        val createdBasket = basketService.createBasket(products)

        assertThat(createdBasket.products.size).isEqualTo(products.size)
        assertThat(createdBasket.numberOfProducts).isEqualTo(products.size)
        assertThat(createdBasket.products).hasSameElementsAs(basketProducts)
    }

    @Test
    fun `getTotalPrice returns the total price of the basket given a valid id`() {
        val totalPrice = basketService.getTotalPrice(basketId)

        assertThat(totalPrice).isEqualTo(BigDecimal(300)) // 1 * 100 + 2 * 100 = 300
    }
}
