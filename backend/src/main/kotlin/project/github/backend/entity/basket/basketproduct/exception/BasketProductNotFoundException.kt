package project.github.backend.entity.basket.basketproduct.exception

import project.github.backend.exceptions.EntityNotFoundException

class BasketProductNotFoundException(basketId: Long, productId: Long) : EntityNotFoundException("Could not find basket product $productId in basket $basketId")