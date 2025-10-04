package ru.cookbook

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CookbookApp

@Suppress("SpreadOperator")
fun main(args: Array<String>) {
    runApplication<CookbookApp>(*args)
}
