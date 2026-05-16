package eu.codeloop.ai.wjug.boundary.inbound.pizza

import assertk.all
import assertk.assertThat
import assertk.assertions.contains
import eu.codeloop.ai.wjug.IntegrationTest
import eu.codeloop.ai.wjug.call
import eu.codeloop.ai.wjug.firstByName
import eu.codeloop.ai.wjug.hasDescription
import eu.codeloop.ai.wjug.isSuccess
import org.junit.jupiter.api.Test

class PizzaMcpIntegrationTest : IntegrationTest() {

    @Test
    fun `list-pizzas tool should have correct description`() {
        // given
        val tool = toolSpecifications.firstByName("list-pizzas")

        // expect
        assertThat(tool).hasDescription("List all available pizzas")
    }

    @Test
    fun `should return all pizzas when calling list-pizzas tool`() {
        // given
        val tool = toolSpecifications.firstByName("list-pizzas")

        // when
        val result = tool.call()

        // then
        assertThat(result).isSuccess().all {
            contains("margherita")
            contains("Margherita")
            contains("pepperoni")
            contains("vegetarian")
            contains("capricciosa")
            contains("quattro-formaggi")
        }
    }
}
