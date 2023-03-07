package project.github.backend

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * When a [Product] related exception this will render an HTTP 404
 */
@ControllerAdvice
class ProductNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(ProductNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun productNotFoundHandler(e: ProductNotFoundException): String {
        return e.message.toString()
    }
}