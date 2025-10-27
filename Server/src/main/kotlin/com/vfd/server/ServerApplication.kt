package com.vfd.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ServerApplication

fun main(args: Array<String>) {
    runApplication<ServerApplication>(*args)

    println("Server is running...")
}
// TODO -> !!!dokończyć zmieniać tekst, dodać zmianę motywu,
//  zastanowić się co z errorami po stronie backendu,
//  poczyścić kod, poprawić żeby było schludnie i przejrzyście tak samo jak np. w Asset!!!
