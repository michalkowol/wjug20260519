package eu.codeloop.ai.wjug.boundary.inbound.error

import eu.codeloop.ai.wjug.boundary.inbound.common.FailReason
import eu.codeloop.ai.wjug.boundary.inbound.common.JSendError
import eu.codeloop.ai.wjug.boundary.inbound.common.JSendFail
import eu.codeloop.ai.wjug.domain.order.OrderNotFoundException
import eu.codeloop.ai.wjug.domain.pizza.PizzaNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ApiExceptionHandler {

    private val log = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(PizzaNotFoundException::class)
    fun handlePizzaNotFound(ex: PizzaNotFoundException): ResponseEntity<JSendFail<FailReason>> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(JSendFail(data = FailReason(ex.message ?: "Pizza not found")))
    }

    @ExceptionHandler(OrderNotFoundException::class)
    fun handleOrderNotFound(ex: OrderNotFoundException): ResponseEntity<JSendFail<FailReason>> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(JSendFail(data = FailReason(ex.message ?: "Order not found")))
    }

    @ExceptionHandler(Exception::class)
    fun handleUnexpected(ex: Exception): ResponseEntity<JSendError> {
        log.error("Unhandled exception [type={}]", ex.javaClass.simpleName, ex)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(JSendError(message = "Internal Server Error"))
    }
}
