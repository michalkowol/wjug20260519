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

## Build & Run

```sh
./gradlew bootRun     # start the application on :8080
./gradlew check       # run unit, integration and ArchUnit tests
./gradlew bootJar     # build a runnable jar (build/libs/app.jar)
```

A `Dockerfile` is provided for container builds.
