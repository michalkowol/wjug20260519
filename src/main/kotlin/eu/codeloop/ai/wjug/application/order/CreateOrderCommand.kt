package eu.codeloop.ai.wjug.application.order

data class CreateOrderCommand(
    val pizzaIds: List<String>,
)
