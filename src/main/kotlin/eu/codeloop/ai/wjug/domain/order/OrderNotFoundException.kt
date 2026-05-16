package eu.codeloop.ai.wjug.domain.order

class OrderNotFoundException(id: String) : RuntimeException("Order not found [id=$id]")
