package project.github.backend.entity.basketproduct.exception

import project.github.backend.exceptions.EntityNotFoundException

class BasketProductNotFoundException(id: Long) : EntityNotFoundException("Could not find basket product $id")