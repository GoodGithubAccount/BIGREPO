package project.github.backend.order

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.ExposesResourceFor
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import project.github.backend.LoadDatabase
import project.github.backend.order.exceptions.IllegalOrderCancellationException
import project.github.backend.order.exceptions.IllegalOrderCompletionException
import project.github.backend.order.exceptions.OrderNotFoundException
import java.util.stream.Collectors

@RestController
@ExposesResourceFor(Order::class)
@RequestMapping("/orders")
class OrderController(
    private val orderService: OrderService,
    val assembler: OrderModelAssembler,
) {
    private val log: Logger = LoggerFactory.getLogger(LoadDatabase::class.java)


    /**
     * TODO KDoc
     */
    @GetMapping
    fun orders(): CollectionModel<*> {
        val ordersStream = orderService.getAllOrders().stream()
        val ordersAsEntityModel = ordersStream.map { order ->
            assembler.toModel(order)
        }.collect(Collectors.toList())

        return CollectionModel.of(
            ordersAsEntityModel, linkTo(methodOn(OrderController::class.java).orders()).withSelfRel()
        )
    }

    /**
     * TODO add 201 created instead of 200 OK
     * TODO KDoc
     */
    @PostMapping
    fun createOrder(@RequestBody payload: OrderRepresentation?): ResponseEntity<*> {
        val order = orderService.createOrder(payload!!)
        val model = assembler.toModel(order)

        //TODO return the created order with a created http code
        return ResponseEntity.ok(model)
    }

    /**
     * Endpoint for retrieving the details of an [Order] with the specified ID.
     * @param id The ID of the [Order] to retrieve.
     * @return An [EntityModel] containing the details of the retrieved product.
     * @throws OrderNotFoundException If no order with the specified [id] is found in the [OrderRepository].
     */
    @GetMapping("/{id}")
    fun getOrder(@PathVariable id: Long): EntityModel<Order> {
        val order = orderService.getOrder(id)
        return assembler.toModel(order)
    }

    /**
     * Endpoint for completing an [Order] by its [id].
     * @param id The ID of the order.
     * @return An HTTP '200 OK' if completed.
     * @throws IllegalOrderCompletionException If the order [Status] is already COMPLETED or CANCELLED.
     */
    @PutMapping("/{id}/complete")
    fun complete(@PathVariable id: Long): ResponseEntity<*> {
        val order = orderService.completeOrder(id)
        return ResponseEntity.ok(assembler.toModel(orderService.save(order)))
    }

    /**
     * Endpoint for cancelling an [Order] with the given [id].
     * @param id The ID of the order.
     * @return An HTTP '200 OK' if cancelled.
     * @throws IllegalOrderCancellationException If the order [Status] is already COMPLETED or CANCELLED.
     */
    @DeleteMapping("/{id}/cancel")
    fun cancel(@PathVariable id: Long): ResponseEntity<*> {
        val order = orderService.cancelOrder(id)
        return ResponseEntity.ok(assembler.toModel(orderService.save(order)))
    }
}
