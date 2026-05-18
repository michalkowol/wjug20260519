package eu.codeloop.ai.wjug.boundary.inbound.web

import eu.codeloop.ai.wjug.application.pizza.PizzaFacade
import eu.codeloop.ai.wjug.boundary.inbound.web.common.JSendSuccess
import eu.codeloop.ai.wjug.domain.pizza.Pizza
import eu.codeloop.ai.wjug.domain.pizza.PizzaId
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class PizzaController(
    private val pizzaFacade: PizzaFacade
) {

    @GetMapping("/api/v1/pizzas")
    fun findAll(): JSendSuccess<List<Pizza>> {
        return JSendSuccess(data = pizzaFacade.findAll())
    }

    @GetMapping("/api/v1/pizzas/{id}")
    fun findById(@PathVariable id: PizzaId): JSendSuccess<Pizza> {
        return JSendSuccess(data = pizzaFacade.getById(id))
    }
}
