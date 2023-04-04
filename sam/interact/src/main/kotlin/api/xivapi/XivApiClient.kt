package cloud.drakon.tempestbot.interact.api.xivapi

import cloud.drakon.tempestbot.interact.Handler.Companion.json
import io.ktor.client.HttpClient
import io.ktor.client.engine.java.Java
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.JsonElement

class XivApiClient(private val ktorClient: HttpClient = HttpClient(Java)) {
    /**
     * @param string The string to search for.
     * @param indexes Search a specific series of indexes separated by commas.
     * @param language Tell the API to handle the request and the response in the specified language.
     * @param columns Allows specific columns to be pulled from the data and exclude the rest of the JSON response.
     */
    suspend fun search(
        string: String,
        indexes: String? = null,
        language: String? = null,
        columns: Array<String>? = null,
    ): JsonElement {
        return json.parseToJsonElement(ktorClient.get("https://xivapi.com/search") {
            url {
                parameters.append("string", string)

                // Global parameters
                if (language != null) {
                    parameters.append("language", language)
                }
                if (columns != null) {
                    parameters.append("columns", columns.joinToString(","))
                }

                if (indexes != null) {
                    parameters.append("indexes", indexes)
                }
            }
        }.bodyAsText())
    }

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
