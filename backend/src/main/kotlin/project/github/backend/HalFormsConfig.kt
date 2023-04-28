package project.github.backend

import org.springframework.context.annotation.Configuration
import org.springframework.hateoas.config.EnableHypermediaSupport

/**
 * Adds  HAL_FORMS support to Spring Boot projects by adding the `EnableHypermediaSupport` annotation.
 */
@Configuration
@EnableHypermediaSupport(type = [EnableHypermediaSupport.HypermediaType.HAL_FORMS])
class HalFormsConfig