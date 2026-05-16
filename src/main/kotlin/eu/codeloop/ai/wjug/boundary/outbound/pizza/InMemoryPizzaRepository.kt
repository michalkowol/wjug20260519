package eu.codeloop.ai.wjug.boundary.outbound.pizza

import eu.codeloop.ai.wjug.domain.pizza.Pizza
import eu.codeloop.ai.wjug.domain.pizza.PizzaId
import eu.codeloop.ai.wjug.domain.pizza.PizzaRepository
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap

@Repository
class InMemoryPizzaRepository : PizzaRepository {

    private val store = ConcurrentHashMap(
        listOf(
            Pizza(
                id = PizzaId("margherita"),
                name = "Margherita",
                ingredients = listOf("tomato", "mozzarella", "basil"),
                price = "41.00",
                diameterCm = 40
            ),
            Pizza(
                id = PizzaId("pepperoni"),
                name = "Pepperoni",
                ingredients = listOf("tomato", "mozzarella", "pepperoni"),
                price = "50.00",
                diameterCm = 40
            ),
            Pizza(
                id = PizzaId("vegetarian"),
                name = "Vegetarian",
                ingredients = listOf("tomato", "mozzarella", "peppers", "mushrooms", "onions", "olives"),
                price = "51.00",
                diameterCm = 40
            ),
            Pizza(
                id = PizzaId("capricciosa"),
                name = "Capricciosa",
                ingredients = listOf("tomato", "mozzarella", "ham", "mushrooms", "artichokes"),
                price = "56.00",
                diameterCm = 40
            ),
            Pizza(
                id = PizzaId("quattro-formaggi"),
                name = "Quattro Formaggi",
                ingredients = listOf("mozzarella", "gorgonzola", "parmesan", "fontina"),
                price = "54.00",
                diameterCm = 40
            )
        ).associateBy { it.id }
    )

    override fun findAll(): List<Pizza> {
        return store.values.toList()
    }

    override fun findById(id: PizzaId): Pizza? {
        return store[id]
    }
}
