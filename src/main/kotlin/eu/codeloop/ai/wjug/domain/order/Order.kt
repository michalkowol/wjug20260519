package eu.codeloop.ai.wjug.domain.order

data class Order(
    val id: String,
    val pizzaIds: List<String>
)
