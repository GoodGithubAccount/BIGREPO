package project.github.backend.exceptions

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.context.request.WebRequest
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import java.time.Instant

/**
 * This class serves as an exception handler for the [MethodArgumentTypeMismatchException]
 * which may be thrown by other controllers or services.
 */
@ControllerAdvice
class MethodArgumentTypeMismatchAdvice {

    private val log: Logger = LoggerFactory.getLogger(MethodArgumentTypeMismatchAdvice::class.java)

    /**
     * Handles all exceptions of type [MethodArgumentTypeMismatchException].
     * @param e The [MethodArgumentTypeMismatchException] instance which is thrown.
     * @return [ErrorDetails] object with the details of the exception.
     */
    @ResponseBody
    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun methodArgumentTypeMismatchHandler(e: MethodArgumentTypeMismatchException, request: WebRequest): ErrorDetails {
        log.warn(e.message)
        val status = HttpStatus.BAD_REQUEST
        return ErrorDetails(
            timestamp = Instant.now().toString(),
            status = status.value(),
            error = status.reasonPhrase,
            message = e.message ?: "",
            path = (request as ServletWebRequest).request.requestURI
        )
    }
}