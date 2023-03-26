package project.github.backend

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

object CorsConfig {
    val ALLOWED_METHODS = arrayOf("GET", "POST", "PUT", "DELETE")
}

@Configuration
class WebConfig : WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods(*CorsConfig.ALLOWED_METHODS)
    }
}