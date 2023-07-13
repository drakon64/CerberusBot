package cloud.drakon.cerberusbot.interact.api.xivapi

import cloud.drakon.cerberusbot.interact.Handler.Companion.json
import io.ktor.client.HttpClient
import io.ktor.client.engine.java.Java
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.JsonElement

class XivApiClient(private val ktorClient: HttpClient = HttpClient(Java)) {
    suspend fun characterSearch(name: String, server: String? = null): JsonElement {
        return json.parseToJsonElement(ktorClient.get("https://xivapi.com/character/search") {
            url {
                parameters.append("name", name.replace(" ", "+"))

                if (server != null) {
                    parameters.append("server", server)
                }
            }
        }.bodyAsText())
    }
}
