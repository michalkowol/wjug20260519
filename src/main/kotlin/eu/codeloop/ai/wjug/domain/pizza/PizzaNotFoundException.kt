package eu.codeloop.ai.wjug.domain.pizza

class PizzaNotFoundException(id: PizzaId) : RuntimeException("Pizza not found [id=$id]")
