package eu.codeloop.ai.wjug.boundary.outbound.pizza

import eu.codeloop.ai.wjug.domain.pizza.Pizza
import eu.codeloop.ai.wjug.domain.pizza.PizzaRepository
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap

@Repository
class InMemoryPizzaRepository : PizzaRepository {

    private val store = ConcurrentHashMap<String, Pizza>()

    override fun findAll(): List<Pizza> = store.values.toList()

    override fun findById(id: String): Pizza? = store[id]

    override fun save(pizza: Pizza) {
        store[pizza.id] = pizza
    }
}
