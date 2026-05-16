package eu.codeloop.ai.wjug.domain.pizza

interface PizzaRepository {
    fun findAll(): List<Pizza>
    fun findById(id: String): Pizza?
    fun save(pizza: Pizza)
}
