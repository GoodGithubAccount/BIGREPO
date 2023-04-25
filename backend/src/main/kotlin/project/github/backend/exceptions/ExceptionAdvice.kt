package project.github.backend.exceptions

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.context.request.WebRequest
import java.time.Instant

/**
 * All custom exceptions thrown by the application should extend this class.
 * @param message The message of the exception.
 * @param status The HTTP status of the exception.
 */
open class Exception(message: String,  val status: HttpStatus) : RuntimeException(message)

/**
 * This class serves as an exception handler for the [Exception] class and all its subclasses
 * which may be thrown by controllers or services.
 */
@ControllerAdvice
class ExceptionAdvice {

    private val log: Logger = LoggerFactory.getLogger(ExceptionAdvice::class.java)

    /**
     * Handles all custom [Exception] instances.
     * @param e The [Exception] instance which is thrown.
     * @return [ErrorDetails] object with the details of the exception.
     */
    @ResponseBody
    @ExceptionHandler(Exception::class)
    fun exceptionHandler(e: Exception, request: WebRequest): ResponseEntity<ErrorDetails> {
        log.warn(e.message)
        val status = e.status
        val errorDetails = ErrorDetails(
            timestamp = Instant.now().toString(),
            status = status.value(),
            error = status.reasonPhrase,
            message = e.message ?: "",
            path = (request as ServletWebRequest).request.requestURI
        )
        return ResponseEntity(errorDetails, status)
    }

}