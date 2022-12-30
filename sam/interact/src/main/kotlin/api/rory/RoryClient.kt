package cloud.drakon.tempestbot.interact.api.rory

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.java.Java
import io.ktor.client.request.get

class RoryClient {
    suspend fun getRory(): Rory {
        return HttpClient(Java).get("https://rory.cat/purr").body()
    }
}
