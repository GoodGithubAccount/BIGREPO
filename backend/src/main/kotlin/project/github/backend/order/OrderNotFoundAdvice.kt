package project.github.backend.order

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class OrderNotFoundAdvice {

    private val log: Logger = LoggerFactory.getLogger(OrderNotFoundAdvice::class.java)

    /**
     * Handles all exceptions of type [OrderNotFoundException].
     * @param e The [OrderNotFoundException] instance which is thrown.
     * @return An [ErrorMessage] representing the message associated with the exception
     * serialized directly into the [ResponseBody] with an HTTP 404 status code.
     */
    @ResponseBody
    @ExceptionHandler(OrderNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun orderNotFoundHandler(e: OrderNotFoundException): ErrorMessage {
        log.warn(e.message)
        return ErrorMessage(e.message.toString())
    }

    data class ErrorMessage(val message: String)
}