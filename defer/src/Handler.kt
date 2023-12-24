@file:OptIn(ExperimentalStdlibApi::class)

package cloud.drakon.dynamisbot

import cloud.drakon.dynamisbot.lib.discord.interaction.InteractionResponse
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.math.BigInteger
import java.security.KeyFactory
import java.security.Signature
import java.security.spec.EdECPoint
import java.security.spec.EdECPublicKeySpec
import java.security.spec.NamedParameterSpec
import kotlin.experimental.and

class Handler : RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {
    private val publicKey = System.getenv("PUBLIC_KEY")
        .hexToByteArray()
        .apply {
            this[size - 1] = this[size - 1].and(127)
        }
        .reversedArray()

    override fun handleRequest(
        input: APIGatewayV2HTTPEvent,
        context: Context,
    ): APIGatewayV2HTTPResponse {
        val response = APIGatewayV2HTTPResponse()

        if (validateRequest(
                input.headers["x-signature-timestamp"]!!,
                input.body,
                input.headers["x-signature-ed25519"]!!.hexToByteArray(),
            )
        ) {
            response.body = Json.encodeToString(InteractionResponse(type = 1))
            response.statusCode = 200
        } else {
            val body = "invalid request signature"

            response.statusCode = 401
            response.body = body

            context.logger.log(body)
        }

        return response
    }

    // https://stackoverflow.com/questions/65780235/ed25519-in-jdk-15-parse-public-key-from-byte-array-and-verify
    // https://github.com/myosyn/DiscordInteraKTions/blob/cd22595534f58fa742ad410545cc6c85739df958/requests-verifier/src/main/kotlin/net/perfectdreams/discordinteraktions/verifier/InteractionRequestVerifier.kt
    private fun validateRequest(timestamp: String, body: String, signature: ByteArray) =
        Signature.getInstance("Ed25519").apply {
            initVerify(
                KeyFactory.getInstance("Ed25519").generatePublic(
                    EdECPublicKeySpec(
                        NamedParameterSpec.ED25519,
                        EdECPoint(
                            publicKey[publicKey.size - 1].toInt().and(255).shr(7) == 1,
                            BigInteger(1, publicKey))
                    )
                )
            )
            update((timestamp + body).toByteArray())
        }.verify(signature)
}
