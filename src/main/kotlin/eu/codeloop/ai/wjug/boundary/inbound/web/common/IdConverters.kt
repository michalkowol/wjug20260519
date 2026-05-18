package eu.codeloop.ai.wjug.boundary.inbound.web.common

import eu.codeloop.ai.wjug.domain.order.OrderId
import eu.codeloop.ai.wjug.domain.pizza.PizzaId
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

@Component
class PizzaIdConverter : Converter<String, PizzaId> {
    override fun convert(source: String): PizzaId {
        return PizzaId(source)
    }
}

@Component
class OrderIdConverter : Converter<String, OrderId> {
    override fun convert(source: String): OrderId {
        return OrderId(source)
    }
}
