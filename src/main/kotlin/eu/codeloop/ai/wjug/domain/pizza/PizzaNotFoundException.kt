package eu.codeloop.ai.wjug.domain.pizza

class PizzaNotFoundException(id: String) : RuntimeException("Pizza not found [id=$id]")
