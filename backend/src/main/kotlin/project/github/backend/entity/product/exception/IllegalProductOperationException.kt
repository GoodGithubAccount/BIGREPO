package project.github.backend.entity.product.exception

import org.springframework.http.HttpStatus
import project.github.backend.exceptions.BackendException

open class IllegalProductOperationException(message: String) : BackendException(message, HttpStatus.METHOD_NOT_ALLOWED)