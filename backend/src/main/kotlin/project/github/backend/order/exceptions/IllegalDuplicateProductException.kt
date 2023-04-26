package project.github.backend.order.exceptions

/**
 * An exception thrown when an order is created two of the same product.
 */
class IllegalDuplicateProductException(productId: String) :
    IllegalOrderOperationException("Failed to create order with duplicate of product $productId")