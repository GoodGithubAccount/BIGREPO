package project.github.backend.order

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class OrderNotFoundAdvice {

    /**
     * Handles all exceptions of type [OrderNotFoundException].
     * @param e The [OrderNotFoundException] instance which is thrown.
     * @return A string representing the error message associated with the exception
     * serialized directly into the [ResponseBody] with an HTTP 404 status code.
     */
    @ResponseBody
    @ExceptionHandler(OrderNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun orderNotFoundHandler(e: OrderNotFoundException): String {
        return e.message.toString()
    }
}