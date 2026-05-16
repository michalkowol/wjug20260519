package eu.codeloop.ai.wjug.application.order

import eu.codeloop.ai.wjug.domain.pizza.PizzaId

data class CreateOrderCommand(val pizzaIds: List<PizzaId>)
