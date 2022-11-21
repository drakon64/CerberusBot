package cloud.drakon.tempestbot.plugins

import io.ktor.server.application.Application
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

fun Application.configureRouting() {
    routing {
        post("/") {

        }
    }
}
