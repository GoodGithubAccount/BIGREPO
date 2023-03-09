package project.github.backend

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.mock.mockito.MockBeans
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(ProductController::class)
@AutoConfigureMockMvc
@MockBeans(
        MockBean(ProductModelAssembler::class),
        MockBean(ProductRepository::class)
)
class ProductControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return ok message`() {
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk)
    }
}