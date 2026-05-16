package eu.codeloop.ai.wjug.boundary.outbound.order

import eu.codeloop.ai.wjug.domain.order.Order
import eu.codeloop.ai.wjug.domain.order.OrderRepository
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap

@Repository
class InMemoryOrderRepository : OrderRepository {

    private val store = ConcurrentHashMap<String, Order>(
        listOf(
            Order(id = "order-1", pizzaIds = listOf("margherita")),
            Order(id = "order-2", pizzaIds = listOf("margherita", "pepperoni")),
            Order(id = "order-3", pizzaIds = listOf("hawaiian", "capricciosa", "quattro-formaggi"))
        ).associateBy { it.id }
    )

    override fun findAll(): List<Order> {
        return store.values.toList()
    }

    override fun findById(id: String): Order? {
        return store[id]
    }

    override fun save(order: Order) {
        store[order.id] = order
    }
}
