package eu.codeloop.ai.wjug.boundary.inbound.error

import eu.codeloop.ai.wjug.domain.order.OrderNotFoundException
import eu.codeloop.ai.wjug.domain.pizza.PizzaNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class NotFoundExceptionHandler {

    @ExceptionHandler(PizzaNotFoundException::class)
    fun handlePizzaNotFound(ex: PizzaNotFoundException): ProblemDetail {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.message)
    }

    @ExceptionHandler(OrderNotFoundException::class)
    fun handleOrderNotFound(ex: OrderNotFoundException): ProblemDetail {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.message)
    }
}
