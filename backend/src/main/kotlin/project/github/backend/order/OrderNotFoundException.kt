package project.github.backend.order

import project.github.backend.exceptions.EntityNotFoundException

/**
 * An exception thrown when a requested [Order] cannot be found.
 * @param id The [Order.id] that could not be found.
 */
class OrderNotFoundException(id: Long) : EntityNotFoundException("Could not find order $id")