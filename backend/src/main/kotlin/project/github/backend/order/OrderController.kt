package project.github.backend.order

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.IanaLinkRelations
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import project.github.backend.LoadDatabase
import project.github.backend.order.exceptions.IllegalOrderCancellationException
import project.github.backend.order.exceptions.IllegalOrderCompletionException
import project.github.backend.order.exceptions.OrderNotFoundException
import java.util.stream.Collectors
import project.github.backend.order.OrderService.NewOrder as NewOrder

@RestController
class OrderController(
    private val orderRepository: OrderRepository,
    private val orderService: OrderService,
    val assembler: OrderModelAssembler
) {
    private val log: Logger = LoggerFactory.getLogger(LoadDatabase::class.java)

    /**
     * Endpoint for Retrieving all [Order]s from the [orderRepository] and returns them as a collection of [EntityModel]s.
     * @return A [CollectionModel] containing [EntityModel]s of all orders in the [orderRepository],
     * along with a self-referencing link.
     */
    @GetMapping("/orders")
    fun all(): CollectionModel<EntityModel<Order>> {
        val ordersStream = this.orderRepository.findAll().stream()
        val ordersAsEntityModel = ordersStream.map { order ->
            assembler.toModel(order)
        }.collect(Collectors.toList())

        return CollectionModel.of(
            ordersAsEntityModel, linkTo(methodOn(OrderController::class.java).all()).withSelfRel()
        )
    }

    /**
     * Endpoint for saving a [newOrder]. Converts the list of [OrderService.OrderItemRequest]s to
     * a list of [OrderItem]s, and saves them to the [orderRepository] as a new [Order].
     * @param newOrder The list of [OrderService.OrderItemRequest]s to convert to a list of [OrderItem].
     * @return A [ResponseEntity] object with the saved [Order] in the response body
     * and a self-referencing link in the response header.
     * The response status code is 201 CREATED.
     */
    @PostMapping("/orders")
    fun newOrder(@RequestBody newOrder: NewOrder): ResponseEntity<*> {
        val order = orderService.createOrder(newOrder)
        val entityModel: EntityModel<Order> = assembler.toModel(order)

        log.info("Sending response: $order")
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
        val order = orderService.getOrder(id)
        return assembler.toModel(order)
    }

    /**
     * Endpoint for completing an [Order] by its [id].
     * @param id The ID of the order.
     * @return An HTTP '200 OK' if completed, or if the order [Status] is already COMPLETED or CANCELLED a '405 METHOD_NOT_ALLOWED'.
     */
    @PutMapping("/orders/{id}/complete")
    fun complete(@PathVariable id: Long): ResponseEntity<*> {
        val order = orderRepository.findById(id).orElseThrow { OrderNotFoundException(id) }

        if (order.getStatus() == Status.COMPLETED || order.getStatus() == Status.CANCELLED) {
            throw IllegalOrderCompletionException(id, order.getStatus())
        }
        order.setStatus(Status.COMPLETED)
        return ResponseEntity.ok(assembler.toModel(orderRepository.save(order)))
    }

    /**
     * Endpoint for cancelling an [Order] with the given [id].
     * @param id The ID of the order.
     * @return An HTTP '200 OK' if cancelled, or if the order [Status] is already CANCELLED or COMPLETED a '405 METHOD_NOT_ALLOWED'.
     */
    @DeleteMapping("/orders/{id}/cancel")
    fun cancel(@PathVariable id: Long): ResponseEntity<*> {
        val order = orderRepository.findById(id).orElseThrow { OrderNotFoundException(id) }

        if (order.getStatus() == Status.CANCELLED || order.getStatus() == Status.COMPLETED) {
            throw IllegalOrderCancellationException(id, order.getStatus())
        }
        order.setStatus(Status.CANCELLED)
        return ResponseEntity.ok(assembler.toModel(orderRepository.save(order)))
    }
}
