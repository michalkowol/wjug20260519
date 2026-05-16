package eu.codeloop.ai.wjug.application.pizza

import eu.codeloop.ai.wjug.domain.pizza.Pizza
import eu.codeloop.ai.wjug.domain.pizza.PizzaId
import eu.codeloop.ai.wjug.domain.pizza.PizzaNotFoundException
import eu.codeloop.ai.wjug.domain.pizza.PizzaRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class PizzaFacade(
    private val pizzaRepository: PizzaRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun findAll(): List<Pizza> {
        log.info("Finding all pizzas")
        return pizzaRepository.findAll()
    }

    fun getById(id: PizzaId): Pizza {
        log.info("Getting pizza by id [id={}]", id)
        return pizzaRepository.findById(id) ?: throw PizzaNotFoundException(id)
    }
}
