package project.github.backend.product

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * This class serves as an exception handler for the [ProductNotFoundException]
 * which may be thrown by other controllers or services.
 */
@ControllerAdvice
class ProductNotFoundAdvice {

    private val log: Logger = LoggerFactory.getLogger(ProductNotFoundAdvice::class.java)

    /**
     * Handles all exceptions of type [ProductNotFoundException].
     * @param e The [ProductNotFoundException] instance which is thrown.
     * @return An [ErrorMessage] representing the message associated with the exception
     * serialized directly into the [ResponseBody] with an HTTP 404 status code.
     */
    @ResponseBody
    @ExceptionHandler(ProductNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun productNotFoundHandler(e: ProductNotFoundException): ErrorMessage {
        log.warn(e.message)
        return ErrorMessage(e.message.toString())
    }

    data class ErrorMessage(val message: String)
}