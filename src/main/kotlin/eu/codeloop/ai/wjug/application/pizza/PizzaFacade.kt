package eu.codeloop.ai.wjug.application.pizza

import eu.codeloop.ai.wjug.domain.pizza.Pizza
import eu.codeloop.ai.wjug.domain.pizza.PizzaNotFoundException
import eu.codeloop.ai.wjug.domain.pizza.PizzaRepository
import org.springframework.stereotype.Service

@Service
class PizzaFacade(
    private val pizzaRepository: PizzaRepository,
) {
    fun findAll(): List<Pizza> = pizzaRepository.findAll()

    fun getById(id: String): Pizza =
        pizzaRepository.findById(id) ?: throw PizzaNotFoundException(id)
}
