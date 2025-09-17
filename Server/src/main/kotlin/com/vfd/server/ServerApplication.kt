package com.vfd.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ServerApplication

fun main(args: Array<String>) {
    runApplication<ServerApplication>(*args)

    println("Server is running...")
}


// TODO: Miękkie usuwanie tylko
// TODO: Pod koniec projektu dodać wszelkie zabezpieczenia dla endpointów oraz zmienić bazę na postgresa
// TODO: Zmiana adresu = dodanie nowego adresu

// TODO: Moderator -> akceptuje wszelkie prośby od strażaków (i ich prośby utworzenia przez użytkowników (User - gotowe) (FirefighterActivity) a strażacy sami sobie je wpisują
// TODO: Moderator -> dodaje sprzęt (Asset) i badania (Inspection), wydarzenia (Event), Działania (Operation) oraz propozycje zakupów (InvestmentProposal)
// TODO: Strażak -> dodaje swoje aktywności (FirefighterActivity) oraz zgłasza propozycje zakupów (InvestmentProposal), może przeglądać wszystko w swojej jednostce
// TODO: Admin -> tworzy jednostki (Firedepartment) i dodaje do niej moderatorów (mejlowa symulacja), dodaje wszelkie typy (asset, activity, inspection, operation) i chyba tyle