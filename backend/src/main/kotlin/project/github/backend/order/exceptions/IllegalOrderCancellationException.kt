package project.github.backend.order.exceptions

import project.github.backend.order.Status

/**
 * An exception thrown when an order cannot be cancelled.
 */
class IllegalOrderCancellationException(orderId: Long, orderStatus: Status) :
    IllegalOrderOperationException("Failed to cancel order $orderId with status $orderStatus")