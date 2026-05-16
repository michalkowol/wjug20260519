package eu.codeloop.ai.wjug.boundary.inbound.pizza

import eu.codeloop.ai.wjug.application.pizza.PizzaFacade
import eu.codeloop.ai.wjug.boundary.inbound.jsonapi.JSON_API_MEDIA_TYPE
import eu.codeloop.ai.wjug.boundary.inbound.jsonapi.JsonApiDocument
import eu.codeloop.ai.wjug.boundary.inbound.jsonapi.JsonApiResource
import eu.codeloop.ai.wjug.boundary.inbound.jsonapi.listDocument
import eu.codeloop.ai.wjug.boundary.inbound.jsonapi.singleDocument
import eu.codeloop.ai.wjug.domain.pizza.Pizza
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal

@RestController
@RequestMapping(produces = [JSON_API_MEDIA_TYPE])
class PizzaController(
    private val pizzaFacade: PizzaFacade
) {

    @GetMapping("/api/v1/pizzas")
    fun findAll(): JsonApiDocument<List<JsonApiResource<PizzaAttributes>>> {
        return listDocument(
            type = TYPE,
            items = pizzaFacade.findAll().map { it.id to it.toAttributes() }
        )
    }

    @GetMapping("/api/v1/pizzas/{id}")
    fun findById(@PathVariable id: String): JsonApiDocument<JsonApiResource<PizzaAttributes>> {
        val pizza = pizzaFacade.getById(id)
        return singleDocument(type = TYPE, id = pizza.id, attributes = pizza.toAttributes())
    }

    private fun Pizza.toAttributes(): PizzaAttributes {
        return PizzaAttributes(name = name, ingredients = ingredients, price = price)
    }

    companion object {
        private const val TYPE = "pizzas"
    }
}

data class PizzaAttributes(
    val name: String,
    val ingredients: List<String>,
    val price: BigDecimal
)
