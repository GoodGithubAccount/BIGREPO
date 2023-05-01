package project.github.backend.basketproduct

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.springframework.test.context.junit.jupiter.SpringExtension
import project.github.backend.entity.basket.Basket
import project.github.backend.entity.basket.basketproduct.BasketProduct
import project.github.backend.entity.basket.basketproduct.BasketProductRepository
import project.github.backend.entity.basket.basketproduct.BasketProductService
import project.github.backend.entity.product.Product
import project.github.backend.entity.product.ProductService
import java.math.BigDecimal
import java.util.*

@ExtendWith(SpringExtension::class)
class BasketProductServiceTest {
    private val basketProductRepository = mock(BasketProductRepository::class.java)
    private val productService = mock(ProductService::class.java)
    private val basketProductService = BasketProductService(basketProductRepository, productService)

    private val productId = "p1"
    private val quantity = 1
    private val basket = Basket(products = emptyList(), numberOfProducts = 0)
    private val basketProduct =
        BasketProduct(productId = productId, quantity = quantity, price = BigDecimal(100), currency = "DKK", basket = basket)
    private val basketId = 1L

    private val product = Product(
        id = productId,
        name = "product 1",
        price = 100,
        currency = "USD",
        rebatePercent = 0,
        rebateQuantity = 0,
        upsellProduct = null
    )

    @BeforeEach
    fun setup() {
        //mock repository operation behaviors
        `when`(basketProductRepository.findById(basketId)).thenReturn(Optional.of(basketProduct))
        `when`(productService.getProduct(productId)).thenReturn(product)
        `when`(basketProductRepository.save(any(BasketProduct::class.java))).thenAnswer { invocation -> invocation.arguments[0] }
    }

    @Test
    fun `getBasketProduct returns the BasketProduct given a valid id`() {
        val foundBasketProduct = basketProductService.getBasketProduct(basketId, basketId)

        assertThat(foundBasketProduct).isEqualTo(basketProduct)
    }

    @Test
    fun `createBasketProduct returns the created BasketProduct given valid id and quantity`() {
        val createdBasketProduct = basketProductService.createBasketProduct(basket, productId, quantity)

        assertThat(createdBasketProduct.productId).isEqualTo(productId)
        assertThat(createdBasketProduct.quantity).isEqualTo(quantity)
        assertThat(createdBasketProduct.price).isEqualTo(product.price.toBigDecimal())
        assertThat(createdBasketProduct.currency).isEqualTo(product.currency)
    }
}