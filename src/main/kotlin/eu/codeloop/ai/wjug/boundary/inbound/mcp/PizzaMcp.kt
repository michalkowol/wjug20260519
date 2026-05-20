package eu.codeloop.ai.wjug.boundary.inbound.mcp

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
        generateOutputSchema = true,
        annotations = McpAnnotations(
            readOnlyHint = true,
            destructiveHint = false,
            idempotentHint = true,
            openWorldHint = false
        )
    )
    fun getAllAvailablePizzas(): PizzasResponse {
        return PizzasResponse(pizzaFacade.findAll().toList())
    }

    data class PizzasResponse(val pizzas: List<Pizza>)
}
