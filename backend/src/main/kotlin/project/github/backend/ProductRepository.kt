package project.github.backend

import org.springframework.data.jpa.repository.JpaRepository

/**
 * This interface is a [JpaRepository] interface for [Product] entities.
 * @property Product The entity type being managed by this repository.
 * @property String The entity's ID.
 */
interface ProductRepository: JpaRepository<Product, String >