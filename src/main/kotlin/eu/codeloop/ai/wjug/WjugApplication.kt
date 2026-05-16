package eu.codeloop.ai.wjug

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WjugApplication

fun main(args: Array<String>) {
    runApplication<WjugApplication>(*args)
}
