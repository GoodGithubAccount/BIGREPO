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
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.server.ResponseStatusException
import java.time.Instant
import java.util.regex.Pattern

/**
 * All custom exceptions thrown by the application should extend this class.
 * @param message The message of the exception.
 * @param status The HTTP status of the exception.
 */
open class BackendException(message: String?, val status: HttpStatus) : ResponseStatusException(status, message)

/**
 * This class serves as an exception handler for the [BackendException] class and all its subclasses
 * which may be thrown by controllers or services.
 */
@ControllerAdvice
class ExceptionAdvice {

    private val log: Logger = LoggerFactory.getLogger(ExceptionAdvice::class.java)

    /**
     * Handles all exceptions of type [MethodArgumentTypeMismatchException].
     * @param e The [MethodArgumentTypeMismatchException] instance which is thrown.
     * @return [ErrorDetails] object with the details of the exception.
     */
    @ResponseBody
    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun methodArgumentTypeMismatchHandler(
        e: MethodArgumentTypeMismatchException,
        request: WebRequest
    ): ResponseEntity<ErrorDetails> {
        return handleException(BackendException(e.message, HttpStatus.BAD_REQUEST), request)
    }

    /**
     * Handles all custom [BackendException] instances.
     * @param e The [BackendException] instance which is thrown.
     * @return [ErrorDetails] object with the details of the exception.
     */
    @ResponseBody
    @ExceptionHandler(BackendException::class)
    fun backendExceptionHandler(e: BackendException, request: WebRequest): ResponseEntity<ErrorDetails> {
        return handleException(e, request)
    }

    /**
     * Formats the exception and returns an [ErrorDetails] object.
     * @param e The [BackendException] instance which is thrown.
     * @param request The [WebRequest] metadata at the time of the exception.
     * @return [ErrorDetails] object with the details of the exception as well as the HTTP status.
     */
    private fun handleException(e: BackendException, request: WebRequest): ResponseEntity<ErrorDetails> {
        log.warn(e.message)
        val status = e.status
        val message = cleanMessage(e.message)

        val errorDetails = ErrorDetails(
            timestamp = Instant.now().toString(),
            status = status.value(),
            error = status.reasonPhrase,
            message = message,
            path = (request as ServletWebRequest).request.requestURI
        )
        return ResponseEntity(errorDetails, status)
    }

    /**
     * Extract message without HTTP status code and status message
     *
     * This changes
     * ```
     * "400 BAD_REQUEST \"Failed to convert value of type 'java.lang.String' to required type 'long'; For input string: \"b\"\""
     * ```
     * to
     * ```
     * "Failed to convert value of type 'java.lang.String' to required type 'long'; For input string: \"b\""
     * ```
     * and
     * ```
     * "404 NOT_FOUND \"Could not find order 0\"",
     * ```
     * to
     * ```
     * "Could not find order 0",
     * ```
     */
    private fun cleanMessage(string: String): String {
        val statusCodeMessagePattern = Pattern.compile("^\\d{3}\\s[A-Z_]+\\s\"|\"$")
        return statusCodeMessagePattern.matcher(string).replaceAll("")
    }
}