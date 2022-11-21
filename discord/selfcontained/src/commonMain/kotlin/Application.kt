package cloud.drakon.tempestbot

import cloud.drakon.tempestbot.plugins.configureRouting
import cloud.drakon.tempestbot.plugins.configureSerialization
import io.ktor.server.application.Application

fun main(args: Array<String>): Unit = io.ktor.server.cio.EngineMain.main(args)

fun Application.module() {
    configureRouting()
    configureSerialization()
}
