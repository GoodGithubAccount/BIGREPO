package project.github.backend

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.*
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = [
            "spring.datasource.url=jdbc:h2:mem:testdb"
        ]
)
class ProductControllerTest(@Autowired val client: TestRestTemplate) {

    @Test
    fun `retrieving all products returns 200`() {
        val entity = client.getForEntity<String>("/products")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun `retrieving a non-existent product returns 404`() {
        val id = "unique-id"
        val entity = client.getForEntity<String>("/products/$id")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }

    @DirtiesContext
    @Test
    fun `saving a product returns the product and saves it`() {
        val productId = "unique-id"
        val product = Product(
                id = productId,
                name = "",
                price = 0,
                currency = "",
                rebateQuantity = 0,
                rebatePercent = 0,
                upsellProduct = "null"
        )

        val postResponse = client.postForObject<Product>("/products", product)

        assertThat(postResponse).isEqualTo(product)
        val getResponse = client.getForObject<Product>("/products/$productId")
        assertThat(getResponse).isEqualTo(product)
    }
}