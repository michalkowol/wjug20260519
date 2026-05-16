package eu.codeloop.ai.wjug.domain.pizza

@JvmInline
value class PizzaId(val value: String) {
    override fun toString(): String {
        return value
    }
}

data class Pizza(
    val id: PizzaId,
    val name: String,
    val ingredients: List<String>,
    val price: String,
    val diameterCm: Int,
    val currency: String = "PLN"
)
