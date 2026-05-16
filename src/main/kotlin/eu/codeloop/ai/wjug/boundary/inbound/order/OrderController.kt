package eu.codeloop.ai.wjug.boundary.inbound.order

import eu.codeloop.ai.wjug.application.order.CreateOrderCommand
import eu.codeloop.ai.wjug.application.order.OrderFacade
import eu.codeloop.ai.wjug.boundary.inbound.jsonapi.JSON_API_MEDIA_TYPE
import eu.codeloop.ai.wjug.boundary.inbound.jsonapi.JsonApiDocument
import eu.codeloop.ai.wjug.boundary.inbound.jsonapi.JsonApiResource
import eu.codeloop.ai.wjug.boundary.inbound.jsonapi.listDocument
import eu.codeloop.ai.wjug.boundary.inbound.jsonapi.singleDocument
import eu.codeloop.ai.wjug.domain.order.Order
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestController
@RequestMapping(
    "/api/v1/orders",
    produces = [JSON_API_MEDIA_TYPE],
    consumes = [JSON_API_MEDIA_TYPE],
)
class OrderController(
    private val orderFacade: OrderFacade,
) {

    @GetMapping(consumes = ["*/*"])
    fun findAll(): JsonApiDocument<List<JsonApiResource<OrderAttributes>>> =
        listDocument(
            type = TYPE,
            items = orderFacade.findAll().map { it.id to it.toAttributes() },
        )

    @GetMapping("/{id}", consumes = ["*/*"])
    fun findById(@PathVariable id: String): JsonApiDocument<JsonApiResource<OrderAttributes>> {
        val order = orderFacade.getById(id)
        return singleDocument(type = TYPE, id = order.id, attributes = order.toAttributes())
    }

    @PostMapping
    fun create(
        @RequestBody request: JsonApiDocument<JsonApiResource<CreateOrderAttributes>>,
    ): ResponseEntity<JsonApiDocument<JsonApiResource<OrderAttributes>>> {
        val command = CreateOrderCommand(pizzaIds = request.data.attributes.pizzaIds)
        val order = orderFacade.create(command)
        val location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(order.id)
            .toUri()
        return ResponseEntity
            .created(location)
            .body(singleDocument(type = TYPE, id = order.id, attributes = order.toAttributes()))
    }

    private fun Order.toAttributes() = OrderAttributes(pizzaIds = pizzaIds)

    companion object {
        private const val TYPE = "orders"
    }
}

data class OrderAttributes(
    val pizzaIds: List<String>,
)

data class CreateOrderAttributes(
    val pizzaIds: List<String>,
)
