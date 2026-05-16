package eu.codeloop.ai.wjug.domain.pizza

import java.math.BigDecimal

data class Pizza(
    val id: String,
    val name: String,
    val ingredients: List<String>,
    val price: BigDecimal
)
