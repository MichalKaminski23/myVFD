package com.vfd.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ServerApplication

fun main(args: Array<String>) {
    runApplication<ServerApplication>(*args)

    println("Server is running...")
}
// Moderator -> akceptuje wszelkie prośby od strażaków (i ich prośby utworzenia przez użytkowników (User - gotowe) (FirefighterActivity) a strażacy sami sobie je wpisują
// Moderator -> dodaje sprzęt (Asset) i badania (Inspection), wydarzenia (Event), Działania (Operation) oraz propozycje zakupów (InvestmentProposal)
// Strażak -> dodaje swoje aktywności (FirefighterActivity) oraz głosuje na propozycje zakupów (InvestmentProposal/votes), może przeglądać wszystko w swojej jednostce
// Admin -> tworzy jednostki (Firedepartment) i dodaje do niej moderatorów (mejlowa symulacja), dodaje wszelkie typy (asset, activity, inspection, operation) i chyba tyle
// MOŻE dodać wybieranie jakiego sprzętu się na akcji użyło MOŻE