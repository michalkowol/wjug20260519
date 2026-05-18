# WJUG 2026-05-19

Spring AI MCP Server: Demo for WJUG 330 (https://www.meetup.com/warszawa-jug/events/314689916/)

## Overview

A simple pizza ordering service built with Kotlin and Spring Boot 4, following
the hexagonal (ports & adapters) architecture. Pizzas and orders are kept in
in-memory repositories seeded at startup.

## API

All responses follow the [JSend](https://github.com/omniti-labs/jsend)
convention (`status` + `data` / `message`). Path variables and request body
fields use the domain value-class IDs directly thanks to dedicated converters
in `boundary/inbound/common/IdConverters.kt`.

| Method | Path                  | Description           |
|--------|-----------------------|-----------------------|
| GET    | `/api/v1/pizzas`      | List all pizzas       |
| GET    | `/api/v1/pizzas/{id}` | Get a pizza by id     |
| GET    | `/api/v1/orders`      | List all orders       |
| GET    | `/api/v1/orders/{id}` | Get an order by id    |
| POST   | `/api/v1/orders`      | Create a new order    |

`POST /api/v1/orders` body:

```json
{ "pizzaIds": ["margherita", "pepperoni"] }
```

Missing pizzas / orders return `404` with a JSend `fail` payload.

## MCP Server

The application exposes a [Model Context Protocol](https://modelcontextprotocol.io/)
server via `spring-ai-starter-mcp-server-webmvc`, allowing LLM clients to
interact with the pizza domain through tool calls.

- Transport: **Streamable HTTP**
- Endpoint: `http://localhost:8080/mcp`

Configured in `application.properties`:

```properties
spring.ai.mcp.server.protocol=streamable
spring.ai.mcp.server.streamable-http.mcp-endpoint=/mcp
```

### Exposed Tools

| Tool           | Description                | Source                                                |
|----------------|----------------------------|-------------------------------------------------------|
| `list-pizzas`  | List all available pizzas  | `boundary/inbound/pizza/PizzaMcp.kt`                  |
| `order-pizzas` | Place an order for pizzas  | `boundary/inbound/order/PizzaOrderMcp.kt`             |

Tools are declared with `@McpTool` on `@Component` beans in the `boundary/inbound`
layer and delegate to the same application facades used by the REST controllers.

### Client Configuration

Example configuration for an MCP client (e.g. Claude Desktop, Claude Code):

```json
{
  "mcpServers": {
    "wjug-pizza": {
      "url": "http://localhost:8080/mcp"
    }
  }
}
```

### Claude

```sh
claude mcp remove pizza
claude mcp add --transport http pizza http://localhost:8080/mcp
# or
claude mcp add --transport http pizza https://wjug.app.codeloop.eu/mcp
```

### Prompts

```text
List all pizzas
Show the results in a table
Use emojis
Use the pizza mcp
```

```text
We have 55 people at a conference. Order pizzas. Reduce the number of Vegetarian, Margherita and Quattro Formaggi
```

```text
Add 5 extra just in case
```

## Build & Run

```sh
./gradlew bootRun     # start the application on :8080
./gradlew check       # run unit, integration and ArchUnit tests
./gradlew bootJar     # build a runnable jar (build/libs/app.jar)
```

A `Dockerfile` is provided for container builds.
