package com.vfd.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ServerApplication

fun main(args: Array<String>) {
    runApplication<ServerApplication>(*args)
    println("Server is running...")
}

/*

{
    "firstName": "Michal",
    "lastName": "Admin",
    "address": {
    "country": "Polska",
    "voivodeship": "Śląsk",
    "city": "Dabie",
    "postalCode": "42-420",
    "street": "Starawieś",
    "houseNumber": "12",
    "apartNumber": ""
},
    "emailAddress": "admin@gmail.com",
    "phoneNumber": "+123321123",
    "password": "ZbychuXPompa12345!"
}

{
  "emailAddress": "admin@gmail.com",
  "password": "ZbychuXPompa12345!"
}

*/
