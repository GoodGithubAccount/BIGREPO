package project.github.backend

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * This class configures WebMvc rules and among other
 * things allows basic CRUD methods from foreign origins.
 */
@Configuration
class WebConfig : WebMvcConfigurer {
    val ALLOWED_METHODS = arrayOf("GET", "POST", "PUT", "DELETE")
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods(*ALLOWED_METHODS)
    }
}