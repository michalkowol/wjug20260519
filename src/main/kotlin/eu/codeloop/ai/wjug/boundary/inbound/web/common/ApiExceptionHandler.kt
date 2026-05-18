package eu.codeloop.ai.wjug.boundary.inbound.web.common

import eu.codeloop.ai.wjug.domain.order.OrderNotFoundException
import eu.codeloop.ai.wjug.domain.pizza.PizzaNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class ApiExceptionHandler : ResponseEntityExceptionHandler() {

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

    override fun handleExceptionInternal(
        ex: Exception,
        body: Any?,
        headers: HttpHeaders,
        statusCode: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        val message = (body as? ProblemDetail)?.detail ?: ex.message ?: "Error"
        log.info("Exception [type={}, message={}, exceptionMessage={}]", ex.javaClass.simpleName, message, ex.message)
        return ResponseEntity.status(statusCode).body(JSendError(message = message))
    }
}
