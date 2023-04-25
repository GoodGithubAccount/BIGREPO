package project.github.backend.order.exceptions

import project.github.backend.exceptions.EntityNotFoundException
import project.github.backend.order.Order

/**
 * An exception thrown when a requested [Order] cannot be found.
 * @param id The [Order.id] that could not be found.
 */
class OrderNotFoundException(id: Long) : EntityNotFoundException("Could not find order $id")