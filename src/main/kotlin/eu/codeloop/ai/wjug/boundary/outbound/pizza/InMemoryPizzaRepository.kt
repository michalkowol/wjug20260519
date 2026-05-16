package eu.codeloop.ai.wjug.boundary.outbound.pizza

import eu.codeloop.ai.wjug.domain.pizza.Pizza
import eu.codeloop.ai.wjug.domain.pizza.PizzaRepository
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.util.concurrent.ConcurrentHashMap

@Repository
class InMemoryPizzaRepository : PizzaRepository {

    private val store = ConcurrentHashMap<String, Pizza>(
        listOf(
            Pizza(
                id = "margherita",
                name = "Margherita",
                ingredients = listOf("tomato", "mozzarella", "basil"),
                price = BigDecimal("25.00")
            ),
            Pizza(
                id = "pepperoni",
                name = "Pepperoni",
                ingredients = listOf("tomato", "mozzarella", "pepperoni"),
                price = BigDecimal("27.00")
            ),
            Pizza(
                id = "hawaiian",
                name = "Hawaiian",
                ingredients = listOf("tomato", "mozzarella", "ham", "pineapple"),
                price = BigDecimal("29.00")
            ),
            Pizza(
                id = "capricciosa",
                name = "Capricciosa",
                ingredients = listOf("tomato", "mozzarella", "ham", "mushrooms", "artichokes"),
                price = BigDecimal("31.00")
            ),
            Pizza(
                id = "quattro-formaggi",
                name = "Quattro Formaggi",
                ingredients = listOf("mozzarella", "gorgonzola", "parmesan", "fontina"),
                price = BigDecimal("33.00")
            )
        ).associateBy { it.id }
    )

    override fun findAll(): List<Pizza> {
        return store.values.toList()
    }

    override fun findById(id: String): Pizza? {
        return store[id]
    }
}
