package com.vfd.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ServerApplication

fun main(args: Array<String>) {
    runApplication<ServerApplication>(*args)
    println("Server is running...")
}

// TODO: dodać kontrolery - w podstawowych przypadkach - potem będziemy dodawać kolejne
// TODO: zastanowić się nad tym jak zrobić usuwanie (miękkie lub nie) członków, pojazdów, sprzętu itp.
// TODO: w przyszłości dodać walidację danych i sprawdzanie czy email istnieje, typy i tak dalej
// TODO: Może dodać tabelę "Wydarzenia", gdzie będzie się wpisywać co się dzieje i tak dalej np. Dzień Ziemniaka - forma bloga