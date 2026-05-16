package eu.codeloop.ai.wjug.application.order

import eu.codeloop.ai.wjug.domain.common.IdGenerator
import eu.codeloop.ai.wjug.domain.order.Order
import eu.codeloop.ai.wjug.domain.order.OrderNotFoundException
import eu.codeloop.ai.wjug.domain.order.OrderRepository
import eu.codeloop.ai.wjug.domain.pizza.PizzaNotFoundException
import eu.codeloop.ai.wjug.domain.pizza.PizzaRepository
import org.springframework.stereotype.Component

@Component
class OrderFacade(
    private val orderRepository: OrderRepository,
    private val pizzaRepository: PizzaRepository,
    private val idGenerator: IdGenerator
) {
    fun findAll(): List<Order> {
        return orderRepository.findAll()
    }

    fun getById(id: String): Order {
        return orderRepository.findById(id) ?: throw OrderNotFoundException(id)
    }

    fun create(command: CreateOrderCommand): Order {
        command.pizzaIds.forEach { pizzaId ->
            pizzaRepository.findById(pizzaId) ?: throw PizzaNotFoundException(pizzaId)
        }
        val order = Order(id = idGenerator.generate(), pizzaIds = command.pizzaIds)
        orderRepository.save(order)
        return order
    }
}
