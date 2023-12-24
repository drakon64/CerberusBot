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

class Handler : RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {
    private val publicKey: String = System.getenv("PUBLIC_KEY")

    override fun handleRequest(
            input: APIGatewayV2HTTPEvent,
            context: Context,
    ): APIGatewayV2HTTPResponse {
        val response = APIGatewayV2HTTPResponse()

        if (validateRequest(input.headers, input.body)) {
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

    private fun validateRequest(headers: Map<String, String>, body: String) = Signature.getInstance("Ed25519").let {
        it.initVerify(KeyFactory.getInstance("EdDSA").generatePublic(EdECPublicKeySpec(NamedParameterSpec("ED25519"), EdECPoint(false, BigInteger(1, publicKey.hexToByteArray())))))
        it.update((headers["x-signature-timestamp"] + body).toByteArray())
        it.verify(headers["x-signature-ed25519"]!!.toByteArray())
    }
}
