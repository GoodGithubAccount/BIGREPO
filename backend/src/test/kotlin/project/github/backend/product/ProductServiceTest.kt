package project.github.backend.product

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.*
import org.springframework.test.context.junit.jupiter.SpringExtension
import project.github.backend.entity.product.Product
import project.github.backend.entity.product.ProductRepository
import project.github.backend.entity.product.ProductRepresentation
import project.github.backend.entity.product.ProductService
import java.math.BigDecimal
import java.util.*

@ExtendWith(SpringExtension::class)
class ProductServiceTest {
    private val productRepository = mock(ProductRepository::class.java)
    private val productService = ProductService(productRepository)

    private val productId = "p1"
    private val productRepresentation = ProductRepresentation().also { productRepresentation ->
        productRepresentation.id = productId
        productRepresentation.name = "product 1"
        productRepresentation.price = BigDecimal(100)
        productRepresentation.currency = "DKK"
        productRepresentation.rebatePercent = BigDecimal(10)
        productRepresentation.rebateQuantity = 2
        productRepresentation.upsellProductId = "p2"
    }

    private val product = Product(
        id = productId,
        name = "product 1",
        price = 100,
        currency = "DKK",
        rebatePercent = 10,
        rebateQuantity = 2,
        upsellProduct = "p2"
    )

    @BeforeEach
    fun setup() {
        `when`(productRepository.findById(productId)).thenReturn(Optional.of(product))
        `when`(productRepository.save(any(Product::class.java))).thenAnswer { invocation -> invocation.arguments[0] }
    }

    @Test
    fun `save with ProductRepresentation returns the saved Product`() {
        val savedProduct = productService.save(productRepresentation)

        assertThat(savedProduct).isEqualTo(product)
    }

    @Test
    fun `save with Product returns the saved Product`() {
        val savedProduct = productService.save(product)

        assertThat(savedProduct).isEqualTo(product)
    }

    @Test
    fun `delete removes the Product`() {
        productService.delete(productId)

        verify(productRepository).deleteById(productId)
    }

    @Test
    fun `updateProduct updates the existing Product`() {
        val newProductRepresentation = ProductRepresentation().also { productRepresentation ->
            productRepresentation.id = productId
            productRepresentation.name = "updated product"
            productRepresentation.price = BigDecimal(200)
            productRepresentation.currency = "DKK"
            productRepresentation.rebatePercent = BigDecimal(20)
            productRepresentation.rebateQuantity = 3
            productRepresentation.upsellProductId = "p3"
        }
        val updatedProduct = product.copy()

        productService.updateProduct(updatedProduct, newProductRepresentation)

        assertThat(updatedProduct).usingRecursiveComparison()
        assertThat(updatedProduct.name).isEqualTo(newProductRepresentation.name)
        assertThat(updatedProduct.price).isEqualTo(newProductRepresentation.price!!.toInt())
        assertThat(updatedProduct.rebatePercent).isEqualTo(newProductRepresentation.rebatePercent!!.toInt())
        assertThat(updatedProduct.rebateQuantity).isEqualTo(newProductRepresentation.rebateQuantity)
        assertThat(updatedProduct.upsellProduct).isEqualTo(newProductRepresentation.upsellProductId)
    }

    @Test
    fun `getProduct returns the Product given a valid id`() {
        val foundProduct = productService.getProduct(productId)

        assertThat(foundProduct).isEqualTo(product)
    }
}
