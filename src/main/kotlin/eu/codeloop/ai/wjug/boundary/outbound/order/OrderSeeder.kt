package eu.codeloop.ai.wjug.boundary.outbound.order

import eu.codeloop.ai.wjug.domain.common.IdGenerator
import eu.codeloop.ai.wjug.domain.order.Order
import eu.codeloop.ai.wjug.domain.order.OrderRepository
import eu.codeloop.ai.wjug.domain.pizza.PizzaRepository
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order as SpringOrder

@Configuration
class OrderSeeder {

    private val log = LoggerFactory.getLogger(OrderSeeder::class.java)

    @Bean
    @SpringOrder(2)
    fun seedOrders(
        orderRepository: OrderRepository,
        pizzaRepository: PizzaRepository,
        idGenerator: IdGenerator,
    ): ApplicationRunner = ApplicationRunner {
        val pizzas = pizzaRepository.findAll()
        if (pizzas.isEmpty()) {
            log.warn("No pizzas available, skipping order seeding")
            return@ApplicationRunner
        }

        val orderPizzaSelections = listOf(
            listOf(pizzas[0]),
            listOf(pizzas[0], pizzas.getOrElse(1) { pizzas[0] }),
            listOf(
                pizzas.getOrElse(2) { pizzas[0] },
                pizzas.getOrElse(3) { pizzas[0] },
                pizzas.getOrElse(4) { pizzas[0] },
            ),
        )

        orderPizzaSelections.forEach { selectedPizzas ->
            val order = Order(
                id = idGenerator.generate(),
                pizzaIds = selectedPizzas.map { it.id },
            )
            orderRepository.save(order)
            log.info(
                "Seeded order [id={}, pizzas={}]",
                order.id,
                selectedPizzas.joinToString(", ") { it.name },
            )
        }
    }
}
