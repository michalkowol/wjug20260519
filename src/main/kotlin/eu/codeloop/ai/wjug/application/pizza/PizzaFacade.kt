package eu.codeloop.ai.wjug.application.pizza

import eu.codeloop.ai.wjug.domain.pizza.Pizza
import eu.codeloop.ai.wjug.domain.pizza.PizzaId
import eu.codeloop.ai.wjug.domain.pizza.PizzaNotFoundException
import eu.codeloop.ai.wjug.domain.pizza.PizzaRepository
import org.springframework.stereotype.Component

@Component
class PizzaFacade(
    private val pizzaRepository: PizzaRepository
) {
    fun findAll(): List<Pizza> {
        return pizzaRepository.findAll()
    }

    fun getById(id: PizzaId): Pizza {
        return pizzaRepository.findById(id) ?: throw PizzaNotFoundException(id)
    }
}
