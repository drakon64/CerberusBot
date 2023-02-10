package cloud.drakon.tempestbot.api.rory

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.java.Java
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json

class RoryClient {
    suspend fun getRory(): Rory {
        return HttpClient(Java) {
            install(ContentNegotiation) {
                json()
            }
        }.get("https://rory.cat/purr").body()
    }
}
