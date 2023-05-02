package project.github.backend

import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.RepresentationModelAssembler
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.stereotype.Component
import project.github.backend.entity.order.OrderController
import project.github.backend.entity.product.ProductController

@Component
class ApiModelAssembler: RepresentationModelAssembler<ApiRoot, EntityModel<ApiRoot>> {
    override fun toModel(entity: ApiRoot): EntityModel<ApiRoot> {
        val model = EntityModel.of(entity)
        model.addSelfLink()
        model.addAvailableLinks()
        return model
    }
}

private fun EntityModel<ApiRoot>.addSelfLink() {
    val controllerClass = ApiController::class.java
    add(linkTo(methodOn(controllerClass).get()).withSelfRel())
}

private fun EntityModel<ApiRoot>.addAvailableLinks() {
    addOrderLink()
    addProductLink()
}

private fun EntityModel<ApiRoot>.addOrderLink() {
    val controllerClass =  OrderController::class.java
    add(linkTo(methodOn(controllerClass).orders()).withRel("orders"))
}

private fun EntityModel<ApiRoot>.addProductLink() {
    val controllerClass =  ProductController::class.java
    add(linkTo(methodOn(controllerClass).all()).withRel("products"))
}