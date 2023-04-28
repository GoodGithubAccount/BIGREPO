package project.github.backend.basket

import org.springframework.data.jpa.repository.JpaRepository

interface BasketRepository : JpaRepository<Basket, Long>