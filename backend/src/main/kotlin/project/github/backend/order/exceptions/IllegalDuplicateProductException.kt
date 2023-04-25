package project.github.backend.order.exceptions

class IllegalDuplicateProductException(productId: String) :
    IllegalOrderOperationException("Failed to create order with duplicate of product $productId")