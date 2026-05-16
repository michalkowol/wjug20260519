package eu.codeloop.ai.wjug.domain.order

interface OrderRepository {
    fun findAll(): List<Order>
    fun findById(id: OrderId): Order?
    fun save(order: Order)
}
