package eu.codeloop.ai.wjug.boundary.inbound.mcp

import eu.codeloop.ai.wjug.application.order.CreateOrderCommand
import eu.codeloop.ai.wjug.application.order.OrderFacade
import eu.codeloop.ai.wjug.domain.pizza.PizzaId
import org.springframework.ai.mcp.annotation.McpTool
import org.springframework.ai.mcp.annotation.McpTool.McpAnnotations
import org.springframework.ai.mcp.annotation.McpToolParam
import org.springframework.stereotype.Component

@Component
class OrderMcp(private val orderFacade: OrderFacade) {

    @McpTool(
        name = "order-pizzas",
        description = "Operation for ordering pizzas",
        generateOutputSchema = true,
        annotations = McpAnnotations(
            readOnlyHint = false,
            destructiveHint = true,
            idempotentHint = false,
            openWorldHint = false
        )
    )
    fun orderPizzas(@McpToolParam(description = "Pizza ids") pizzaIds: List<String>): OrderResponse {
        val ids = pizzaIds.map { PizzaId(it) }
        val order = orderFacade.create(CreateOrderCommand(ids))
        return OrderResponse(
            id = order.id.value,
            pizzaIds = order.pizzaIds.map { it.value }
        )
    }

    data class OrderResponse(val id: String, val pizzaIds: List<String>)
}
