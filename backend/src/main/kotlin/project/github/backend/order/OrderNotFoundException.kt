package project.github.backend.order

/**
 * An exception thrown when a requested order cannot be found.
 * @param id The ID of the order that could not be found.
 */
class OrderNotFoundException(id: Long) : RuntimeException("Could not find order $id")
