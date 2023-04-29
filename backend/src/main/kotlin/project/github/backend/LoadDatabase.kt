package project.github.backend

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import project.github.backend.basket.Basket
import project.github.backend.basketproduct.BasketProduct
import project.github.backend.order.*
import project.github.backend.product.Product
import project.github.backend.product.ProductRepository

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
        productRepository: ProductRepository,
        orderRepository: OrderRepository,
        orderService: OrderService
    ) = CommandLineRunner {
        println("Preloading...")
        val p1 = Product("vitamin-d-90-100", "D-vitamin, 90ug, 100 stk", 116, "DKK", 3, 10, null)
        val p2 = Product("vitamin-c-500-250", "C-vitamin, 500mg, 250 stk", 150, "DKK", 2, 25, "vitamin-c-depot-500-250")
        val o1 = Order(
            basket = Basket(
                products = listOf(
                    BasketProduct(
                        productId = p1.id,
                        quantity = 2,
                        price = (p1.price).toBigDecimal()
                    ),
                    BasketProduct(
                        productId = p2.id,
                        quantity = 3,
                        price = (p2.price).toBigDecimal()
                    )
                ),
                numberOfProducts = 1,
                currency = "DKK"
            ),
            totalPrice = (p1.price * 2).toBigDecimal(),
            currency = "DKK",
            status = Status.IN_PROGRESS
        )
        val o2 = Order(
            basket = Basket(
                products = listOf(
                    BasketProduct(
                        productId = p1.id,
                        quantity = 5,
                        price = (p1.price).toBigDecimal()
                    )
                ),
                numberOfProducts = 1,
                currency = "DKK"
            ),
            totalPrice = (p1.price * 2).toBigDecimal(),
            currency = "DKK",
            status = Status.IN_PROGRESS
        )

        log.info("Preloading " + productRepository.save(p1))
        log.info("Preloading " + productRepository.save(p2))
        log.info("Preloading " + orderRepository.save(o1))
        log.info("Preloading " + orderRepository.save(o2))
    }
}