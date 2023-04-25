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
import java.time.Instant

/**
 * An exception thrown when an entity cannot be found.
 * @param message The message of the exception.
 */
open class EntityNotFoundException(message: String) : RuntimeException(message)

/**
 * This class serves as an exception handler for the [EntityNotFoundException]
 * which may be thrown by other controllers or services.
 */
@ControllerAdvice
class EntityNotFoundAdvice {

    private val log: Logger = LoggerFactory.getLogger(EntityNotFoundAdvice::class.java)

    /**
     * Handles all exceptions of type [EntityNotFoundException].
     * @param e The [EntityNotFoundException] instance which is thrown.
     * @return [ErrorDetails] object with the details of the exception.
     */
    @ResponseBody
    @ExceptionHandler(EntityNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun entityNotFoundHandler(e: EntityNotFoundException, request: WebRequest): ErrorDetails {
        log.warn(e.message)
        val status = HttpStatus.NOT_FOUND
        return ErrorDetails(
            timestamp = Instant.now().toString(),
            status = status.value(),
            error = status.reasonPhrase,
            message = e.message ?: "",
            path = (request as ServletWebRequest).request.requestURI
        )
    }
}