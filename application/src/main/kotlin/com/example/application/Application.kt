package com.example.application

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication(scanBasePackages = ["com.example"])
@EnableScheduling
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}