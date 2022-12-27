package cloud.drakon.tempestbot.interact.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.java.Java
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

class XivApiClient(private val ktorClient: HttpClient = HttpClient(Java)) {
    private fun Boolean.toBinaryString(): String {
        return if (this) {
            "1"
        } else {
            "0"
        }
    }

    /**
     * @param indexes Search a specific series of indexes separated by commas.
     * @param string The string to search for. The results for this are affected by `string_column` and `string_algo`.
     * @param stringAlgo The search algorithm to use for string matching. **Default:** wildcard
     */
    suspend fun search(
        string: String,
        indexes: String? = null,
        stringAlgo: String? = null,
        language: String? = null,
        snakeCase: Boolean? = null,
        privateKey: String? = null,
    ): JsonElement {
        return Json.parseToJsonElement(ktorClient.get("https://xivapi.com/search") {
            url {
                parameters.append("string", string)

                // Global parameters
                if (language != null) {
                    parameters.append("language", language)
                }
                if (snakeCase != null) {
                    parameters.append("snake_case", snakeCase.toBinaryString())
                }
                if (privateKey != null) {
                    parameters.append("private_key", privateKey)
                }

                if (indexes != null) {
                    parameters.append("indexes", indexes)
                }
                if (stringAlgo != null) {
                    parameters.append("string_algo", stringAlgo)
                }
            }
        }.bodyAsText())
    }

    suspend fun item(
        item: Int,
        language: String? = null,
        snakeCase: Boolean? = null,
        privateKey: String? = null,
    ): JsonElement {
        return Json.parseToJsonElement(ktorClient.get("https://xivapi.com/item/$item") {
            url { // Global parameters
                if (language != null) {
                    parameters.append("language", language)
                }
                if (snakeCase != null) {
                    parameters.append("snake_case", snakeCase.toBinaryString())
                }
                if (privateKey != null) {
                    parameters.append("private_key", privateKey)
                }
            }
        }.bodyAsText())
    }

    suspend fun characterSearch(name: String, server: String): JsonElement {
        return Json.parseToJsonElement(ktorClient.get("https://xivapi.com/character/search") {
            url {
                parameters.append("name", name.replace(" ", "+"))
                parameters.append("server", server)
            }
        }.bodyAsText())
    }
}
