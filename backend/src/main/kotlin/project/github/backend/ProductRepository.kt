package project.github.backend

import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository: JpaRepository<Product, String >