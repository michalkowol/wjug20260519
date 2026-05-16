package eu.codeloop.ai.wjug.boundary.inbound.order

import assertk.all
import assertk.assertThat
import assertk.assertions.contains
import eu.codeloop.ai.wjug.IntegrationTest
import eu.codeloop.ai.wjug.call
import eu.codeloop.ai.wjug.firstByName
import eu.codeloop.ai.wjug.hasDescription
import eu.codeloop.ai.wjug.isError
import eu.codeloop.ai.wjug.isSuccess
import org.junit.jupiter.api.Test

class PizzaOrderMcpIntegrationTest : IntegrationTest() {

    @Test
    fun `order-pizzas tool should have correct description`() {
        // given
        val tool = toolSpecifications.firstByName("order-pizzas")

        // expect
        assertThat(tool).hasDescription("Operation for ordering pizzas")
    }

    @Test
    fun `should create order when calling order-pizzas tool`() {
        // given
        val tool = toolSpecifications.firstByName("order-pizzas")

        // when
        val result = tool.call(mapOf("pizzaIds" to listOf("margherita", "pepperoni")))

        // then
        assertThat(result).isSuccess().all {
            contains("margherita")
            contains("pepperoni")
            contains("\"id\"")
        }
    }

    @Test
    fun `should return error when ordering non-existent pizza`() {
        // given
        val tool = toolSpecifications.firstByName("order-pizzas")

        // when
        val result = tool.call(mapOf("pizzaIds" to listOf("non-existent")))

        // then
        assertThat(result).isError().all {
            contains("Pizza not found")
            contains("non-existent")
        }
    }
}
