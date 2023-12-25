package cloud.drakon.dynamisbot

import cloud.drakon.dynamisbot.commands.universalis
import cloud.drakon.dynamisbot.lib.discord.applicationcommand.ApplicationCommandOption
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
import kotlin.system.exitProcess
import kotlinx.serialization.json.JsonElement

internal val ephemeral = ApplicationCommandOption(
    type = 5,
    name = "ephemeral",
    description = "Make the bot response visible to you only"
)

suspend fun main() = println(
    HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }.put("https://discord.com/api/v10/applications/${System.getenv("APPLICATION_ID")}/commands") {
        headers {
            append(HttpHeaders.Authorization, "Bot ${System.getenv("BOT_TOKEN")}")
        }

        contentType(ContentType.Application.Json)
        setBody(arrayOf(universalis))
    }.let {
        println(it.body<String>())
        if (it.status.value != 200) exitProcess(1)
    }
)
