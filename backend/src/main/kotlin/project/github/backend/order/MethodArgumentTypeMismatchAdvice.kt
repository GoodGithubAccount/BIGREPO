package project.github.backend.order

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

/**
 * This class serves as an exception handler for the [MethodArgumentTypeMismatchException]
 * which may be thrown by other controllers or services.
 *
 * It is mainly used for [Order] requests with an invalid ID.
 */
@ControllerAdvice
class MethodArgumentTypeMismatchAdvice {

    /**
     * Handles all exceptions of type [MethodArgumentTypeMismatchException].
     * @param ex The [MethodArgumentTypeMismatchException] instance which is thrown.
     * @return A string representing the error message associated with the exception
     * serialized directly into the [ResponseBody] with an HTTP 400 status code.
     */
    @ResponseBody
    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun methodArgumentTypeMismatchHandler(ex: MethodArgumentTypeMismatchException): String {
        return "Invalid argument ${ex.value}"
    }
}