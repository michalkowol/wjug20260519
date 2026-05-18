# Live Coding Cheat Sheet — Spring AI MCP Server

Goal: turn the existing REST pizza app into an MCP server, exposing two tools
(`list-pizzas`, `order-pizzas`) over Streamable HTTP. Each step is incremental
— the app stays runnable between steps.

## Step 1 — Add Spring AI dependency

File: `build.gradle.kts`

Add the Spring AI BOM and the MCP server starter:

```kotlin
dependencyManagement {
    imports {
        mavenBom("org.springframework.ai:spring-ai-bom:2.0.0-M6")
    }
}

dependencies {
    // ...
    implementation("org.springframework.ai:spring-ai-starter-mcp-server-webmvc")
    // ...
}
```

> Refresh Gradle. App still starts — no tools yet.

## Step 2 — Configure the MCP endpoint

File: `src/main/resources/application.properties`

Append:

```properties
spring.ai.mcp.server.protocol=streamable
spring.ai.mcp.server.streamable-http.mcp-endpoint=/mcp

# TODO Remove after official Spring AI 2.0.0 release
logging.level.org.apache.tomcat.util.net.NioEndpoint=OFF
logging.level.org.springframework.context.support.PostProcessorRegistrationDelegate=ERROR
```

> Endpoint is now live at `POST http://localhost:8080/mcp`, but exposes zero tools.

## Step 3 — First tool: `list-pizzas` (read-only)

New file: `src/main/kotlin/eu/codeloop/ai/wjug/boundary/inbound/mcp/PizzaMcp.kt`

```kotlin
package eu.codeloop.ai.wjug.boundary.inbound.mcp

import eu.codeloop.ai.wjug.application.pizza.PizzaFacade
import eu.codeloop.ai.wjug.domain.pizza.Pizza
import org.springframework.ai.mcp.annotation.McpTool
import org.springframework.ai.mcp.annotation.McpTool.McpAnnotations
import org.springframework.stereotype.Component

@Component
class PizzaMcp(private val pizzaFacade: PizzaFacade) {

    @McpTool(
        name = "list-pizzas",
        description = "List all available pizzas",
        annotations = McpAnnotations(
            readOnlyHint = true,
            destructiveHint = false,
            idempotentHint = true,
            openWorldHint = false
        )
    )
    fun getAllAvailablePizzas(): List<Pizza> {
        return pizzaFacade.findAll().toList()
    }
}
```

> Restart. `tools/list` now returns one tool.

## Step 4 — Second tool: `order-pizzas` (mutating)

New file: `src/main/kotlin/eu/codeloop/ai/wjug/boundary/inbound/mcp/OrderMcp.kt`

```kotlin
package eu.codeloop.ai.wjug.boundary.inbound.mcp

import eu.codeloop.ai.wjug.application.order.CreateOrderCommand
import eu.codeloop.ai.wjug.application.order.OrderFacade
import eu.codeloop.ai.wjug.domain.order.Order
import eu.codeloop.ai.wjug.domain.pizza.PizzaId
import org.springframework.ai.mcp.annotation.McpTool
import org.springframework.ai.mcp.annotation.McpTool.McpAnnotations
import org.springframework.stereotype.Component

@Component
class OrderMcp(private val orderFacade: OrderFacade) {

    @McpTool(
        name = "order-pizzas",
        description = "Operation for ordering pizzas",
        annotations = McpAnnotations(
            readOnlyHint = false,
            destructiveHint = true,
            idempotentHint = false,
            openWorldHint = false
        )
    )
    fun orderPizzas(pizzaIds: List<String>): Order {
        val ids = pizzaIds.map { PizzaId(it) }
        return orderFacade.create(CreateOrderCommand(ids))
    }
}
```

> Restart. Two tools visible.

## Step 5 — Connect a real MCP client (live demo finale)

Claude Code:

```sh
claude mcp remove pizza
claude mcp add --transport http pizza http://localhost:8080/mcp
```

Prompts to try:

```text
List all pizzas. Show the results in a table. Use emojis. Use the pizza mcp.
```

```text
We have 55 people at a conference. Order pizzas. Reduce the number of
Vegetarian, Margherita and Quattro Formaggi.
```

```text
Add 5 extra just in case.
```
