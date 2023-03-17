package cloud.drakon.tempestbot.interact.api.openai

import api.openai.CreateImageRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.java.Java
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class OpenAI(private val apiKey: String) {
    private val ktorClient = HttpClient(Java) {
        defaultRequest {
            url("https://api.openai.com/v1/")
            contentType(ContentType.Application.Json)
        }

        install(Auth) {
            bearer {
                BearerTokens(apiKey, apiKey)
            }
        }
    }

    suspend fun createImage(request: CreateImageRequest): CreateImageResponse {
        return ktorClient.post("images/generations") {
            setBody(request)
        }.body() as CreateImageResponse
    }
}
