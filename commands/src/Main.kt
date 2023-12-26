package cloud.drakon.dynamisbot

import cloud.drakon.dynamisbot.commands.eorzeaDatabaseCommand
import cloud.drakon.dynamisbot.commands.universalisCommand
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.headers
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import java.io.File
import kotlin.system.exitProcess
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

suspend fun main() {
    val commands = arrayOf(eorzeaDatabaseCommand(), universalisCommand())

    File("commands.json").printWriter().use {
        it.println(Json {
            prettyPrint = true
        }.encodeToString(commands))
    }

    println(
        HttpClient {
            install(ContentNegotiation) {
                json()
            }
        }.put("https://discord.com/api/v10/applications/${System.getenv("APPLICATION_ID")}/commands") {
            headers {
                append(HttpHeaders.Authorization, "Bot ${System.getenv("BOT_TOKEN")}")
            }

            contentType(ContentType.Application.Json)
            setBody(commands)
        }.let {
            if (it.status.value != 200) {
                println(it.body<String>())
                exitProcess(1)
            }
        }
    )
}
