package eu.codeloop.ai.wjug.boundary.outbound.order

import eu.codeloop.ai.wjug.domain.order.Order
import eu.codeloop.ai.wjug.domain.order.OrderRepository
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap

@Repository
class InMemoryOrderRepository : OrderRepository {

    private val store = ConcurrentHashMap<String, Order>()

    override fun findAll(): List<Order> = store.values.toList()

    override fun findById(id: String): Order? = store[id]

    override fun save(order: Order) {
        store[order.id] = order
    }
}
