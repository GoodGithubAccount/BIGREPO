package project.github.backend

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.*
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext
import kotlin.random.Random

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = [
        "spring.datasource.url=jdbc:h2:mem:testdb"
    ]
)
class ProductControllerTest(@Autowired val client: TestRestTemplate, @Autowired val repository: ProductRepository) {

    @BeforeEach
    fun setup() {
        repository.deleteAll()
        assertThat(repository.findAll()).isEmpty()
    }

    @Test
    fun `getting all products returns 200`() {
        val entity = getForEntityAllProducts()
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun `getting a non-existent product by ID returns 404`() {
        val id = Random.nextInt().toString()
        val entity = getEntityProduct(id)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }

    @DirtiesContext
    @Test
    fun `deleting a product returns 204`() {
        val productId = Random.nextInt().toString()
        val product = Product(
            id = productId,
            name = "",
            price = 0,
            currency = "",
            rebateQuantity = 0,
            rebatePercent = 0,
            upsellProduct = null
        )

        repository.save(product)

        val response = client.exchange(
            "/products/{id}",
            HttpMethod.DELETE,
            null,
            Void::class.java,
            productId
        )
        assertThat(response.statusCode).isEqualTo(HttpStatus.NO_CONTENT)
        assertThat(repository.findById(productId)).isEmpty
    }

    @DirtiesContext
    @Test
    fun `upsellProduct can be string`() {
        val stringUpsellId = Random.nextInt().toString()
        val upsellString = Random.nextInt().toString()
        val stringUpsellProduct = Product(
            id = stringUpsellId,
            name = "",
            price = 0,
            currency = "",
            rebateQuantity = 0,
            rebatePercent = 0,
            upsellProduct = upsellString
        )

        postProduct(stringUpsellProduct)

        val product = repository.findById(stringUpsellId).get()
        assertThat(product.getUpsellProduct()).isEqualTo(upsellString)
    }

    @DirtiesContext
    @Test
    fun `upsellProduct can be null`() {
        val nullUpsellId = Random.nextInt().toString()
        val nullUpsellProduct = Product(
            id = nullUpsellId,
            name = "",
            price = 0,
            currency = "",
            rebateQuantity = 0,
            rebatePercent = 0,
            upsellProduct = null
        )

        postProduct(nullUpsellProduct)

        val product = repository.findById(nullUpsellId).get()
        assertThat(product.getUpsellProduct()).isNull()
    }

    @DirtiesContext
    @Test
    fun `posting a product returns the product and saves it`() {
        val productId = Random.nextInt().toString()
        val product = Product(
            id = productId,
            name = "",
            price = 0,
            currency = "",
            rebateQuantity = 0,
            rebatePercent = 0,
            upsellProduct = "null"
        )

        val postResponse = postProduct(product)

        assertThat(postResponse).isEqualTo(product)
        val productInRepository = repository.findById(productId).get()
        assertThat(productInRepository).isEqualTo(product)
    }

    @DirtiesContext
    @Test
    fun `products with same ID are equal`() {
        val productId = Random.nextInt().toString()
        val product = Product(
            id = productId,
            name = "",
            price = 0,
            currency = "",
            rebateQuantity = 0,
            rebatePercent = 0,
            upsellProduct = null
        )

        val newProduct = Product(
            id = productId,
            name = "Jeffrey Bezos",
            price = 3,
            currency = "USD",
            rebateQuantity = 5,
            rebatePercent = 10,
            upsellProduct = "another-id"
        )

        assertThat(product).isEqualTo(newProduct)
    }

    @DirtiesContext
    @Test
    fun `getting a product that was saved and removed will return 404`() {
        val productId = Random.nextInt().toString()
        val product = Product(
            id = productId,
            name = "",
            price = 0,
            currency = "",
            rebateQuantity = 0,
            rebatePercent = 0,
            upsellProduct = "null"
        )

        postProduct(product)

        deleteProduct(productId)

        val getResponse = getEntityProduct(productId)

        assertThat(getResponse.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }

    private fun getEntityProduct(productId: String) = client.getForEntity<String>("/products/$productId")
    private fun deleteProduct(productId: String) = client.delete("/products/$productId")
    private fun postProduct(product: Product) = client.postForObject<Product>("/products", product)
    private fun getForEntityAllProducts() = client.getForEntity<String>("/products")
}