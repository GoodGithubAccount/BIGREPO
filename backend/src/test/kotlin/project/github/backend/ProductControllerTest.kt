package project.github.backend

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.web.client.*
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.http.MediaType
import org.springframework.http.RequestEntity.*
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import project.github.backend.entity.order.*
import project.github.backend.entity.product.*
import java.math.BigDecimal

@WebMvcTest(ProductController::class)
class ProductControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var productService: ProductService

    @MockBean
    private lateinit var assembler: ProductModelAssembler

    private val product1 = Product("p1", "Product 1", 100, "DKK", 2, 10, "p2")
    private val product2 = Product("p2", "Product 2", 200, "DKK", 3, 15, null)

    private val productRepresentation = ProductRepresentation().apply {
        id = "p1"
        name = "Product 1"
        price = BigDecimal(100)
        currency = "DKK"
        rebateQuantity = 2
        rebatePercent = BigDecimal(10)
        upsellProductId = "p2"
    }

    @BeforeEach
    fun setup() {
        //mock ProductService methods
        mockGetAllProducts()
        mockSave()

        //mock ProductModelAssembler methods
        mockToModel()
    }

    private fun mockGetAllProducts() {
        `when`(productService.getAllProducts()).thenReturn(listOf(product1, product2))
    }

    private fun mockSave() {
        `when`(
            productService.save(
                any(Product::class.java) ?: Product(
                    "UNINITIALIZED", "Uninitialized", 0, "UIN", 0, 0, "UNINITIALIZED"
                )
            )
        ).thenAnswer { invocation ->
            val productArg = invocation.arguments[0] as Product
            if (productArg.id == product1.id) {
                product1.copy(
                    name = productArg.name,
                    price = productArg.price,
                    currency = productArg.currency,
                    rebateQuantity = productArg.rebateQuantity,
                    rebatePercent = productArg.rebatePercent,
                    upsellProduct = productArg.upsellProduct
                )
            } else {
                productArg
            }
        }
        `when`(
            productService.save(
                any(ProductRepresentation::class.java) ?: ProductRepresentation()
            )
        ).thenAnswer { invocation ->
            val productArg = invocation.arguments[0] as ProductRepresentation
            if (productArg.id == product1.id) {
                product1.copy(
                    name = productArg.name!!,
                    price = productArg.price!!.toInt(),
                    currency = productArg.currency!!,
                    rebateQuantity = productArg.rebateQuantity!!,
                    rebatePercent = productArg.rebatePercent!!.toInt(),
                    upsellProduct = productArg.upsellProductId
                )
            } else {
                productArg
            }
        }
    }

    private fun mockToModel() {
        `when`(
            assembler.toModel(
                any(Product::class.java) ?: Product(
                    "UNINITIALIZED", "Uninitialized", 0, "UIN", 0, 0, "UNINITIALIZED"
                )
            )
        ).thenAnswer { invocation ->
            val product = invocation.arguments[0] as Product
            if (product.name == "Updated Product 1") {
                EntityModel.of(product).apply {
                    add(linkTo(methodOn(ProductController::class.java).getProduct(product.id)).withSelfRel())
                }
            } else {
                EntityModel.of(product).apply {
                    add(linkTo(methodOn(ProductController::class.java).getProduct(product.id)).withSelfRel())
                }
            }
        }
    }

    @Test
    fun `all returns all products`() {
        mockMvc.perform(get("/products")).andExpect(status().isOk)
            .andExpect(jsonPath("$._embedded.productList[0].id").value(product1.id))
            .andExpect(jsonPath("$._embedded.productList[1].id").value(product2.id))
    }
    
    @Test
    fun `newProduct returns the created product`() {
        mockMvc.perform(
            post("/products").contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(productRepresentation))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").value(product1.id))
    }

    @Test
    fun `getProduct returns a product by ID`() {
        mockGetProduct()
        mockMvc.perform(get("/products/p1"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(product1.id))
    }

    @Test
    fun `replaceProduct updates the product`() {
        mockGetProduct()
        mockUpdateProduct()

        val updatedProductRepresentation = ProductRepresentation().apply {
            id = product1.id
            name = "Updated Product 1"
            price = BigDecimal(150)
            currency = "DKK"
            rebateQuantity = 3
            rebatePercent = BigDecimal(15)
            upsellProductId = null
        }

        mockMvc.perform(
            put("/products/{id}", product1.id).contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(updatedProductRepresentation))
        )
            .andExpect(status().isCreated).andExpect(jsonPath("$.id").value(product1.id))
            .andExpect(jsonPath("$.name").value(updatedProductRepresentation.name))
    }

    private fun mockGetProduct() {
        `when`(productService.getProduct("p1")).thenAnswer {
            if (product1.name == "Updated Product 1") {
                product1.copy(
                    name = "Updated Product 1",
                    price = 150,
                    currency = "DKK",
                    rebateQuantity = 3,
                    rebatePercent = 15,
                    upsellProduct = null
                )
            } else {
                product1
            }
        }
    }

    private fun mockUpdateProduct() {
        `when`(
            productService.updateProduct(
                anyString(),
                any(ProductRepresentation::class.java) ?: ProductRepresentation()
            )
        ).thenAnswer { invocation ->
            val existingProductId = invocation.arguments[0] as String
            val newProduct = invocation.arguments[1] as ProductRepresentation

            val existingProduct = when (existingProductId) {
                product1.id -> product1
                product2.id -> product2
                else -> throw IllegalArgumentException("Invalid product ID")
            }

            existingProduct.name = newProduct.name!!
            existingProduct.price = newProduct.price!!.toInt()
            existingProduct.currency = newProduct.currency!!
            existingProduct.rebateQuantity = newProduct.rebateQuantity!!
            existingProduct.rebatePercent = newProduct.rebatePercent!!.toInt()
            existingProduct.upsellProduct = newProduct.upsellProductId

            existingProduct
        }
    }


    @Test
    fun `deleteProduct deletes the product and returns no content`() {
        mockMvc.perform(delete("/products/p1"))
            .andExpect(status().isNoContent)
    }
}
