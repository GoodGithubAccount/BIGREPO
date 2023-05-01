package project.github.backend.entity.product.exception

import project.github.backend.exceptions.EntityNotFoundException
import project.github.backend.entity.product.Product

/**
 * An exception thrown when a requested [Product] cannot be found.
 * @param id The [Product.id] that could not be found.
 */
class ProductNotFoundException(id: String) : EntityNotFoundException("Could not find product $id")