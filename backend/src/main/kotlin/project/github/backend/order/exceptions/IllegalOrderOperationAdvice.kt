package project.github.backend.order.exceptions

import org.springframework.http.HttpStatus
import project.github.backend.exceptions.Exception

/**
 * An exception thrown when an illegal order operation is attempted.
 * @param message The message of the exception.
 */
open class IllegalOrderOperationException(message: String) : Exception(message, HttpStatus.METHOD_NOT_ALLOWED)