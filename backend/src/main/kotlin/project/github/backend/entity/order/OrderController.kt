package project.github.backend.entity.order

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.mediatype.hal.HalModelBuilder
import org.springframework.hateoas.server.ExposesResourceFor
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*
import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import project.github.backend.LoadDatabase
import project.github.backend.entity.order.exceptions.IllegalOrderCancellationException
import project.github.backend.entity.order.exceptions.IllegalOrderCompletionException
import project.github.backend.entity.order.exceptions.OrderNotFoundException

@RestController
@ExposesResourceFor(Order::class)
@RequestMapping("/orders")
class OrderController(
        private val orderService: OrderService,
        private val assembler: OrderModelAssembler,
) {
    private val log: Logger = LoggerFactory.getLogger(LoadDatabase::class.java)


    /**
     * TODO KDoc
     */
    @GetMapping
    fun orders(): HttpEntity<*> {
        val allOrders = orderService.getAllOrders()

        val proxyControllerClass = methodOn(OrderController::class.java)

        val selfLink = linkTo(proxyControllerClass.orders()).withSelfRel()
                .andAffordance(afford(proxyControllerClass.createOrder(null)))

        val findLink = linkTo(proxyControllerClass.getOrder(null)).withRel("find")

        val orderLinks = allOrders.map { order ->
            linkTo(proxyControllerClass.getOrder(order.id)).withRel("product")
                    .withTitle(order.id.toString())
        }

        val model = HalModelBuilder.emptyHalModel()
                .link(selfLink)
                .link(findLink)
                .links(orderLinks)
                .build()

        return ResponseEntity.ok(model)
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
    fun getOrder(@PathVariable id: Long?): EntityModel<Order> {
        if (id == null) {
            throw OrderNotFoundException(null)
        }
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
    fun complete(@PathVariable id: Long?): HttpEntity<*> {
        if (id == null) {
            throw OrderNotFoundException(null)
        }

        orderService.completeOrder(id).id

        return getOrder(id)
    }

    /**
     * Endpoint for cancelling an [Order] with the given [id].
     * @param id The ID of the order.
     * @return An HTTP '200 OK' if cancelled.
     * @throws IllegalOrderCancellationException If the order [Status] is already COMPLETED or CANCELLED.
     */
    @DeleteMapping("/{id}/cancel")
    fun cancel(@PathVariable id: Long?): HttpEntity<*> {
        if (id == null) {
            throw OrderNotFoundException(null)
        }

        orderService.cancelOrder(id)

        return getOrder(id)
    }
}
