package com.example.application

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.example"])
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}