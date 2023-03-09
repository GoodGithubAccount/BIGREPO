package project.github.backend

/**
 * An exception thrown when a requested product cannot be found.
 * @param id the ID of the product that could not be found.
 */
class ProductNotFoundException(id: String) : RuntimeException("Could not find product $id")