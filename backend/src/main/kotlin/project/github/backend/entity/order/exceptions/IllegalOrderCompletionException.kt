package project.github.backend.entity.order.exceptions

import project.github.backend.entity.order.Status

/**
 * An exception thrown when an order cannot be completed.
 */
class IllegalOrderCompletionException(orderId: Long, orderStatus: Status) :
    IllegalOrderOperationException("Failed to complete order $orderId with status $orderStatus")