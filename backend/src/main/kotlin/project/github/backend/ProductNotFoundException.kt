package project.github.backend

class ProductNotFoundException(id: String) : RuntimeException("Could not find product $id")