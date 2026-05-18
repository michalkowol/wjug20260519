package eu.codeloop.ai.wjug.boundary.inbound.web

import eu.codeloop.ai.wjug.IntegrationTest
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.get

class PizzaControllerIntegrationTest : IntegrationTest() {

    @Test
    fun `should return all pizzas`() {
        mvc.get("/api/v1/pizzas").andExpect {
            status { isOk() }
            jsonPath("$.status") { value("success") }
            jsonPath("$.data.length()") { value(5) }
            jsonPath("$.data[?(@.id == 'margherita')].name") { value("Margherita") }
        }
    }

    @Test
    fun `should return pizza by id`() {
        mvc.get("/api/v1/pizzas/margherita").andExpect {
            status { isOk() }
            jsonPath("$.status") { value("success") }
            jsonPath("$.data.id") { value("margherita") }
            jsonPath("$.data.name") { value("Margherita") }
            jsonPath("$.data.price") { value("41.00") }
            jsonPath("$.data.currency") { value("PLN") }
        }
    }

    @Test
    fun `should return 404 when pizza not found`() {
        mvc.get("/api/v1/pizzas/non-existent").andExpect {
            status { isNotFound() }
            jsonPath("$.status") { value("fail") }
            jsonPath("$.data.message") { value("Pizza not found [id=non-existent]") }
        }
    }
}
