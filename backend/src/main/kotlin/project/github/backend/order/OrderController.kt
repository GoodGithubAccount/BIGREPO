package project.github.backend.order

import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.IanaLinkRelations
import org.springframework.hateoas.MediaTypes
import org.springframework.hateoas.mediatype.problem.Problem
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.stream.Collectors

@RestController
class OrderController(private val repository: OrderRepository, private val assembler: OrderModelAssembler) {

    /**
     * Endpoint for Retrieving all [Order]s from the [repository] and returns them as a collection of [EntityModel]s.
     * @return A [CollectionModel] containing [EntityModel]s of all orders in the [repository],
     * along with a self-referencing link.
     */
    @GetMapping("/orders")
    fun all(): CollectionModel<EntityModel<Order>> {
        val ordersStream = this.repository.findAll().stream()
        val ordersAsEntityModel = ordersStream.map { order ->
            assembler.toModel(order)
        }.collect(Collectors.toList())

        return CollectionModel.of(
            ordersAsEntityModel, linkTo(methodOn(OrderController::class.java).all()).withSelfRel()
        )
    }

    /**
     * Endpoint for saving a given [Order] instance to the [OrderRepository].
     * @param newOrder The [Order] entity to be saved.
     * @return A [ResponseEntity] object with the saved [Order] in the response body
     * and a self-referencing link in the response header.
     * The response status code is 201 (Created).
     */
    @PostMapping("/orders")
    fun newOrder(@RequestBody newOrder: Order): ResponseEntity<*> {
        newOrder.setStatus(Status.IN_PROGRESS)
        val entityModel: EntityModel<Order> = assembler.toModel(repository.save(newOrder))

        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
            .body<EntityModel<Order>>(entityModel)
    }

    /**
     * Endpoint for retrieving the details of an [Order] with the specified ID.
     * @param id The ID of the [Order] to retrieve.
     * @return An [EntityModel] containing the details of the retrieved product.
     * @throws OrderNotFoundException If no order with the specified [id] is found in the [OrderRepository].
     */
    @GetMapping("/orders/{id}")
    fun getOrder(@PathVariable id: Long): EntityModel<Order> {
        val order = repository.findById(id).orElseThrow { OrderNotFoundException(id) }
        return assembler.toModel(order)
    }

    /**
     * Endpoint for completing an [Order] by its [id].
     * @param id The ID of the order.
     * @return An HTTP '200 OK' if completed, or if the order [Status] is already COMPLETED or CANCELLED a '405 METHOD_NOT_ALLOWED'.
     */
    @PutMapping("/orders/{id}/complete")
    fun complete(@PathVariable id: Long): ResponseEntity<*> {
        val order = repository.findById(id).orElseThrow { OrderNotFoundException(id) }

        if (order.getStatus() == Status.COMPLETED || order.getStatus() == Status.CANCELLED) {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE).body(
                    Problem.create().withTitle("Method not allowed")
                        .withDetail("You can not complete an order that is in the ${order.getStatus()} status")
                )
        }
        order.setStatus(Status.COMPLETED)
        return ResponseEntity.ok(assembler.toModel(repository.save(order)))
    }

    /**
     * Endpoint for cancelling an [Order] with the given [id].
     * @param id The ID of the order.
     * @return An HTTP '200 OK' if cancelled, or if the order [Status] is already CANCELLED or COMPLETED a '405 METHOD_NOT_ALLOWED'.
     */
    @DeleteMapping("/orders/{id}/cancel")
    fun cancel(@PathVariable id: Long): ResponseEntity<*> {
        val order = repository.findById(id).orElseThrow { OrderNotFoundException(id) }

        if (order.getStatus() == Status.CANCELLED || order.getStatus() == Status.COMPLETED) {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE).body(
                    Problem.create().withTitle("Method not allowed")
                        .withDetail("You can not cancel an order that is in the ${order.getStatus()} status")
                )
        }
        order.setStatus(Status.CANCELLED)
        return ResponseEntity.ok(assembler.toModel(repository.save(order)))
    }
}
