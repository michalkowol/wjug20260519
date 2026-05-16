package eu.codeloop.ai.wjug.domain.order

interface OrderRepository {
    fun findAll(): List<Order>
    fun findById(id: String): Order?
    fun save(order: Order)
}
