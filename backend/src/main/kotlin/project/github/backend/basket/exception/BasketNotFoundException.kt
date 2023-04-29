package project.github.backend.basket.exception

import project.github.backend.exceptions.EntityNotFoundException
import project.github.backend.basket.Basket

/**
 * An exception thrown when a requested [Basket] cannot be found.
 * @param id The [Basket.id] that could not be found.
 */
class BasketNotFoundException(id: Long) : EntityNotFoundException("Could not find basket $id")