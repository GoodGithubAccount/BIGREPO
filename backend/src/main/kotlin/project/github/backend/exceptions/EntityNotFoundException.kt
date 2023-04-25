package project.github.backend.exceptions

import org.springframework.http.HttpStatus

/**
 * An exception thrown when an entity cannot be found.
 * @param message The message of the exception.
 */
open class EntityNotFoundException(message: String) : Exception(message, HttpStatus.NOT_FOUND)