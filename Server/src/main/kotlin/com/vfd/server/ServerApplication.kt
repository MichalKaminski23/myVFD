package com.vfd.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ServerApplication

fun main(args: Array<String>) {
    runApplication<ServerApplication>(*args)

    println("Server is running...")
}
// TODO -> AdminPanel -> Symulacja mejlowa dotycząca tworzenia nowych OSP np. Grzesiu Firanka pisze mejla do admina, on tworzy OSP i przypisuje mu PRESIDENTA
// TODO -> AdminPanel -> Tworzenie i edycja typów