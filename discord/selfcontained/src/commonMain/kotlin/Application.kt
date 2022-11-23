package cloud.drakon.tempestbot

import cloud.drakon.tempestbot.plugins.DiscordInteractionsPlugin
import cloud.drakon.tempestbot.plugins.configureRouting
import cloud.drakon.tempestbot.plugins.configureSerialization
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.cio.EngineMain

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    install(DiscordInteractionsPlugin)
    configureRouting()
    configureSerialization()
}
