package project.github.backend.order.exceptions

import org.springframework.http.HttpStatus
import project.github.backend.exceptions.BackendException

/**
 * An exception thrown when an illegal order operation is attempted.
 * @param message The message of the exception.
 */
open class IllegalOrderOperationException(message: String) : BackendException(message, HttpStatus.METHOD_NOT_ALLOWED)