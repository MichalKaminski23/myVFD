package com.vfd.server

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ServerApplication

private val logger = LoggerFactory.getLogger(ServerApplication::class.java)

fun main(args: Array<String>) {
    runApplication<ServerApplication>(*args)

    logger.info("✅ Server is running... ✅")
}