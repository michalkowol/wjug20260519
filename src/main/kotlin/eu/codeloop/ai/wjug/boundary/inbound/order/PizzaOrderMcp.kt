package eu.codeloop.ai.wjug.boundary.inbound.order

import eu.codeloop.ai.wjug.application.order.CreateOrderCommand
import eu.codeloop.ai.wjug.application.order.OrderFacade
import eu.codeloop.ai.wjug.domain.order.Order
import eu.codeloop.ai.wjug.domain.pizza.PizzaId
import org.springframework.ai.mcp.annotation.McpTool
import org.springframework.ai.mcp.annotation.McpTool.McpAnnotations
import org.springframework.stereotype.Component

@Component
class PizzaOrderMcp(private val orderFacade: OrderFacade) {

    @McpTool(
        name = "order-pizzas",
        description = "Operation for ordering pizzas",
        annotations = McpAnnotations(
            readOnlyHint = false,
            idempotentHint = false,
            openWorldHint = false,
            destructiveHint = false
        )
    )
    fun orderPizzas(pizzaIds: List<String>): Order {
        val ids = pizzaIds.map { PizzaId(it) }
        return orderFacade.create(CreateOrderCommand(ids))
    }
}
