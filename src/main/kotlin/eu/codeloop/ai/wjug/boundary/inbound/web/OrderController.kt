package eu.codeloop.ai.wjug.boundary.inbound.web

import eu.codeloop.ai.wjug.application.order.CreateOrderCommand
import eu.codeloop.ai.wjug.application.order.OrderFacade
import eu.codeloop.ai.wjug.boundary.inbound.web.common.JSendSuccess
import eu.codeloop.ai.wjug.domain.order.Order
import eu.codeloop.ai.wjug.domain.order.OrderId
import eu.codeloop.ai.wjug.domain.pizza.PizzaId
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
class OrderController(
    private val orderFacade: OrderFacade
) {

    @GetMapping("/api/v1/orders")
    fun findAll(): JSendSuccess<List<Order>> {
        return JSendSuccess(data = orderFacade.findAll())
    }

    @GetMapping("/api/v1/orders/{id}")
    fun findById(@PathVariable id: OrderId): JSendSuccess<Order> {
        return JSendSuccess(data = orderFacade.getById(id))
    }

    @PostMapping("/api/v1/orders")
    fun create(@RequestBody request: CreateOrderRequest): ResponseEntity<JSendSuccess<Order>> {
        val command = CreateOrderCommand(pizzaIds = request.pizzaIds)
        val order = orderFacade.create(command)
        val location = URI.create("/api/v1/orders/${order.id.value}")
        return ResponseEntity.created(location).body(JSendSuccess(data = order))
    }
}

data class CreateOrderRequest(val pizzaIds: List<PizzaId>)
