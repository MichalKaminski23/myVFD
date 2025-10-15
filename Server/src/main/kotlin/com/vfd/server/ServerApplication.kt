package com.vfd.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ServerApplication

fun main(args: Array<String>) {
    runApplication<ServerApplication>(*args)

    println("Server is running...")
}
// TODO -> FirefighterActivity (i type), Admin Panel (symulacja mejlowa -> User prosi Admina "stwórz OSP Strzyżowice i zrób mnie moderatorem",
//  admin to robi i mamy to),
//  tworzenie/edycja typów i chyba tyle (poprawki w kodzie, ujednolicenie kodu, refektoryzacja)