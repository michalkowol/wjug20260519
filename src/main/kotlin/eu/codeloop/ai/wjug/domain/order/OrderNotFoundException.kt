package eu.codeloop.ai.wjug.domain.order

class OrderNotFoundException(id: OrderId) : RuntimeException("Order not found [id=$id]")
