package project.github.backend

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import project.github.backend.entity.order.*
import project.github.backend.entity.product.Product
import project.github.backend.entity.product.ProductRepository

/**
 * This class initializes the database.
 */
@Configuration
class LoadDatabase {

    private val log: Logger = LoggerFactory.getLogger(LoadDatabase::class.java)

    /**
     * Returns a [CommandLineRunner] instance which is used to run the initialization code
     * after the application context has been loaded.
     * @param productRepository A [ProductRepository] instance which is used to save the pre-defined data.
     * @param orderRepository An [OrderRepository] instance which is used to save the pre-defined data.
     */
    @Bean
    fun initDatabase(
        productRepository: ProductRepository, orderRepository: OrderRepository, orderService: OrderService
    ) = CommandLineRunner {
        println("Preloading...")
        
        val p1 = Product("vitamin-d-90-100", "D-vitamin, 90ug, 100 stk", 116, "DKK", 3, 10, null)
        val p2 = Product("vitamin-c-500-250", "C-vitamin, 500mg, 250 stk", 150, "DKK", 2, 25, "vitamin-c-depot-500-250")
        log.info("Preloading " + productRepository.save(p1))
        log.info("Preloading " + productRepository.save(p2))

        val or1 = OrderRepresentation()
        or1.products = mapOf(p1.id to 2, p2.id to 3)
        orderService.createOrder(or1)

        val or2 = OrderRepresentation()
        or2.products = mapOf(p2.id to 1)
        orderService.createOrder(or2)


    }
}