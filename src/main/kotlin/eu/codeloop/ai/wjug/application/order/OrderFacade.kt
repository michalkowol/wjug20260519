package eu.codeloop.ai.wjug.application.order

import eu.codeloop.ai.wjug.domain.common.IdGenerator
import eu.codeloop.ai.wjug.domain.order.Order
import eu.codeloop.ai.wjug.domain.order.OrderId
import eu.codeloop.ai.wjug.domain.order.OrderNotFoundException
import eu.codeloop.ai.wjug.domain.order.OrderRepository
import eu.codeloop.ai.wjug.domain.pizza.PizzaNotFoundException
import eu.codeloop.ai.wjug.domain.pizza.PizzaRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class OrderFacade(
    private val orderRepository: OrderRepository,
    private val pizzaRepository: PizzaRepository,
    private val idGenerator: IdGenerator
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun findAll(): List<Order> {
        log.info("Finding all orders")
        return orderRepository.findAll()
    }

    fun getById(id: OrderId): Order {
        log.info("Getting order by id [id={}]", id)
        return orderRepository.findById(id) ?: throw OrderNotFoundException(id)
    }

    fun create(command: CreateOrderCommand): Order {
        log.info("Creating order [pizzaIds={}]", command.pizzaIds)
        command.pizzaIds.forEach { pizzaId ->
            pizzaRepository.findById(pizzaId) ?: throw PizzaNotFoundException(pizzaId)
        }
        val order = Order(id = OrderId(idGenerator.generate()), pizzaIds = command.pizzaIds)
        orderRepository.save(order)
        log.info("Order created [id={}]", order.id)
        return order
    }
}
