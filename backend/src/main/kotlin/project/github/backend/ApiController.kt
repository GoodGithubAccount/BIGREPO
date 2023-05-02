package project.github.backend

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.hateoas.EntityModel
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("")
class ApiController(
    private val assembler: ApiModelAssembler
) {
    private val log: Logger = LoggerFactory.getLogger(ApiController::class.java)
    @GetMapping("/")
    fun get(): EntityModel<ApiRoot> {
        log.info("Getting API discover endpoint")
        return assembler.toModel(ApiRoot())
    }
}

class ApiRoot