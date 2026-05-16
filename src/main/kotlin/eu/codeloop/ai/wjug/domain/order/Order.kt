package eu.codeloop.ai.wjug.domain.order

import eu.codeloop.ai.wjug.domain.pizza.PizzaId

@JvmInline
value class OrderId(val value: String) {
    override fun toString(): String {
        return value
    }
}

data class Order(
    val id: OrderId,
    val pizzaIds: List<PizzaId>
)
