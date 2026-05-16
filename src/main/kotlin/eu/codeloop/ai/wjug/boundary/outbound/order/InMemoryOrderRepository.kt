package eu.codeloop.ai.wjug.boundary.outbound.order

import eu.codeloop.ai.wjug.domain.order.Order
import eu.codeloop.ai.wjug.domain.order.OrderId
import eu.codeloop.ai.wjug.domain.order.OrderRepository
import eu.codeloop.ai.wjug.domain.pizza.PizzaId
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap

@Repository
class InMemoryOrderRepository : OrderRepository {

    private val store = ConcurrentHashMap(
        listOf(
            Order(id = OrderId("8qyha3"), pizzaIds = listOf(PizzaId("margherita"))),
            Order(id = OrderId("84tain"), pizzaIds = listOf(PizzaId("margherita"), PizzaId("pepperoni"))),
            Order(
                id = OrderId("67fi6j"),
                pizzaIds = listOf(PizzaId("vegetarian"), PizzaId("capricciosa"), PizzaId("quattro-formaggi"))
            )
        ).associateBy { it.id }
    )

    override fun findAll(): List<Order> {
        return store.values.toList()
    }

    override fun findById(id: OrderId): Order? {
        return store[id]
    }

    override fun save(order: Order) {
        store[order.id] = order
    }
}
