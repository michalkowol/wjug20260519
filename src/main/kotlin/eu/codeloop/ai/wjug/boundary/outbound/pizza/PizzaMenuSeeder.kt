package eu.codeloop.ai.wjug.boundary.outbound.pizza

import eu.codeloop.ai.wjug.domain.common.IdGenerator
import eu.codeloop.ai.wjug.domain.pizza.Pizza
import eu.codeloop.ai.wjug.domain.pizza.PizzaRepository
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.math.BigDecimal

@Configuration
class PizzaMenuSeeder {

    private val log = LoggerFactory.getLogger(PizzaMenuSeeder::class.java)

    @Bean
    fun seedPizzaMenu(
        pizzaRepository: PizzaRepository,
        idGenerator: IdGenerator,
    ): ApplicationRunner = ApplicationRunner {
        listOf(
            "Margherita" to listOf("tomato", "mozzarella", "basil"),
            "Pepperoni" to listOf("tomato", "mozzarella", "pepperoni"),
            "Hawaiian" to listOf("tomato", "mozzarella", "ham", "pineapple"),
            "Capricciosa" to listOf("tomato", "mozzarella", "ham", "mushrooms", "artichokes"),
            "Quattro Formaggi" to listOf("mozzarella", "gorgonzola", "parmesan", "fontina"),
        ).forEachIndexed { index, (name, ingredients) ->
            val pizza = Pizza(
                id = idGenerator.generate(),
                name = name,
                ingredients = ingredients,
                price = BigDecimal("${25 + index * 2}.00"),
            )
            pizzaRepository.save(pizza)
            log.info("Seeded pizza [id={}, name={}]", pizza.id, pizza.name)
        }
    }
}
