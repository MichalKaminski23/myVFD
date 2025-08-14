package com.vfd.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ServerApplication

fun main(args: Array<String>) {
    runApplication<ServerApplication>(*args)
    println("Server is running...")
}

// TODO: Miękkie usuwanie lub twarde usuwanie - czyli czy usuwamy na stałe czy tylko oznaczamy jako usunięte np. sprzęt aby go nie móc edytować czy coś
// TODO: Szczegółowe walidacje danych np. email, liczby i tak dalej
// TODO: Tabela "Events" - gdzie będą się wpisywać wydarzenia, które się dzieją w okolicy, np. Dzień Ziemniaka, Dzień Dziecka i tak dalej - jak blog
// TODO: W przyszłości ogarnąć wszelkie zabezpieczenia z logowania i tak dalej
// TODO: Reszta bardziej szczegółowych serwisów i endpointów