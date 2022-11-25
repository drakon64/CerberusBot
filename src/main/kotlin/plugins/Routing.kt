package cloud.drakon.tempestbot.plugins

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Application.configureRouting() {
    routing {
        route("/") {
            this@configureRouting.install(DiscordInteractionsPlugin)
            post {

            }
        }
        get("/healthcheck") {
            call.response.status(HttpStatusCode.OK)
        }
    }
}
