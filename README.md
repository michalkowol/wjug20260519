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

| Tool           | Description                | Source                                |
|----------------|----------------------------|---------------------------------------|
| `list-pizzas`  | List all available pizzas  | `boundary/inbound/mcp/PizzaMcp.kt`    |
| `order-pizzas` | Place an order for pizzas  | `boundary/inbound/mcp/OrderMcp.kt`    |

Tools are declared with `@McpTool` on `@Component` beans in the `boundary/inbound`
layer and delegate to the same application facades used by the REST controllers.

### Tool Annotations (Hints)

Each `@McpTool` carries `McpAnnotations` that describe the tool's behavior to the
client. Clients (and LLMs) use these hints to decide e.g. whether a call needs
user confirmation or whether the result can be cached.

| Hint              | Meaning                                                | `list-pizzas` | `order-pizzas` |
|-------------------|--------------------------------------------------------|---------------|----------------|
| `readOnlyHint`    | Tool does not modify state                             | `true`        | `false`        |
| `destructiveHint` | Tool may delete or overwrite data                      | `false`       | `true`         |
| `idempotentHint`  | Repeating with same args produces the same effect      | `true`        | `false`        |
| `openWorldHint`   | Tool reaches into an open/unbounded external world     | `false`       | `false`        |

Pizza-domain rationale:

- **`list-pizzas`** — pure read against the in-memory catalog. Same input always
  returns the same output, no side effects, no external calls.
- **`order-pizzas`** — creates a new `Order`, so not read-only. Each call mints a
  fresh `OrderId`, so it is **not** idempotent. All state lives in our own
  repositories, hence `openWorldHint = false`. We mark it
  `destructiveHint = true` because, although the call is a pure INSERT in our
  data model, in the real world an order has irreversible side effects (money
  charged, kitchen started) — the hint tells the client to ask the user before
  firing it.

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

## Live Coding

See [CHEATSHEET.md](CHEATSHEET.md) for the step-by-step plan used during the
WJUG 330 demo.
