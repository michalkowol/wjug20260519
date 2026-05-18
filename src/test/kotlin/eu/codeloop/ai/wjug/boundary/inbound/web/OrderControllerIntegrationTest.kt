package eu.codeloop.ai.wjug.boundary.inbound.web

import eu.codeloop.ai.wjug.IntegrationTest
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

class OrderControllerIntegrationTest : IntegrationTest() {

    @Test
    fun `should return all orders`() {
        mvc.get("/api/v1/orders").andExpect {
            status { isOk() }
            jsonPath("$.status") { value("success") }
            jsonPath("$.data[?(@.id == '8qyha3')].pizzaIds[0]") { value("margherita") }
            jsonPath("$.data[?(@.id == '84tain')].pizzaIds[1]") { value("pepperoni") }
        }
    }

    @Test
    fun `should return order by id`() {
        mvc.get("/api/v1/orders/84tain").andExpect {
            status { isOk() }
            jsonPath("$.status") { value("success") }
            jsonPath("$.data.id") { value("84tain") }
            jsonPath("$.data.pizzaIds.length()") { value(2) }
            jsonPath("$.data.pizzaIds[0]") { value("margherita") }
            jsonPath("$.data.pizzaIds[1]") { value("pepperoni") }
        }
    }

    @Test
    fun `should return 404 when order not found`() {
        mvc.get("/api/v1/orders/non-existent").andExpect {
            status { isNotFound() }
            jsonPath("$.status") { value("fail") }
            jsonPath("$.data.message") { value("Order not found [id=non-existent]") }
        }
    }

    @Test
    fun `should create order`() {
        mvc.post("/api/v1/orders") {
            contentType = MediaType.APPLICATION_JSON
            content = """{"pizzaIds": ["margherita", "pepperoni"]}"""
        }.andExpect {
            status { isCreated() }
            header { exists("Location") }
            jsonPath("$.status") { value("success") }
            jsonPath("$.data.id") { isNotEmpty() }
            jsonPath("$.data.pizzaIds[0]") { value("margherita") }
            jsonPath("$.data.pizzaIds[1]") { value("pepperoni") }
        }
    }

    @Test
    fun `should return 404 when creating order with non-existent pizza`() {
        mvc.post("/api/v1/orders") {
            contentType = MediaType.APPLICATION_JSON
            content = """{"pizzaIds": ["non-existent"]}"""
        }.andExpect {
            status { isNotFound() }
            jsonPath("$.status") { value("fail") }
            jsonPath("$.data.message") { value("Pizza not found [id=non-existent]") }
        }
    }
}
