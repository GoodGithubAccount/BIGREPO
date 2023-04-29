package project.github.backend.entity.order.exceptions

import project.github.backend.entity.order.Status

/**
 * An exception thrown when an order cannot be cancelled.
 */
class IllegalOrderCancellationException(orderId: Long, orderStatus: Status) :
    IllegalOrderOperationException("Failed to cancel order $orderId with status $orderStatus")