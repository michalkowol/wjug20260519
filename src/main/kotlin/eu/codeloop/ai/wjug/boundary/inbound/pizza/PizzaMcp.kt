package eu.codeloop.ai.wjug.boundary.inbound.pizza

import eu.codeloop.ai.wjug.application.pizza.PizzaFacade
import eu.codeloop.ai.wjug.domain.pizza.Pizza
import org.springframework.ai.mcp.annotation.McpTool
import org.springframework.ai.mcp.annotation.McpTool.McpAnnotations
import org.springframework.stereotype.Component

@Component
class PizzaMcp(private val pizzaFacade: PizzaFacade) {

    @McpTool(
        name = "list-pizzas",
        description = "List all available pizzas",
        annotations = McpAnnotations(
            readOnlyHint = true,
            idempotentHint = true,
            openWorldHint = false,
            destructiveHint = false
        )
    )
    fun getAllAvailablePizzas(): List<Pizza> {
        return pizzaFacade.findAll().toList()
    }
}
