package cloud.drakon.dynamisbot

import cloud.drakon.dynamisbot.commands.universalisCommand
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
import java.io.File
import kotlin.system.exitProcess
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

suspend fun main() {
    val commands = arrayOf(universalisCommand())

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

internal suspend fun ephemeralCommand(): ApplicationCommandOption {
    val name = "ephemeral"
    val description = "Make the bot response visible to you only"

    return ApplicationCommandOption(
        type = 5,
        name = name,
        nameLocalizations = buildLocalizationMap(name, true),
        description = description,
        descriptionLocalizations = buildLocalizationMap(description),
    )
}

internal fun String.commandName() = this.lowercase().replace(" ", "_")
