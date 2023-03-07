package project.github.backend

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class LoadDatabase {

    private val log: Logger = LoggerFactory.getLogger(LoadDatabase::class.java)

    @Bean
    fun initDatabase(repository: ProductRepository) = CommandLineRunner {
        log.info("Preloading " + repository.save(Product("vitamin-d-90-100", "D-vitamin, 90ug, 100 stk", 116, "DKK", 3, 10, null)))
        log.info("Preloading " + repository.save(Product("vitamin-c-500-250", "C-vitamin, 500mg, 250 stk", 150, "DKK", 2, 25, "vitamin-c-depot-500-250")))
    }
}