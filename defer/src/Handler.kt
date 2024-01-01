@file:OptIn(ExperimentalStdlibApi::class)

package cloud.drakon.dynamisbot

import aws.sdk.kotlin.services.lambda.LambdaClient
import aws.sdk.kotlin.services.lambda.model.InvocationType
import aws.sdk.kotlin.services.lambda.model.InvokeRequest
import cloud.drakon.ktdiscord.interaction.Interaction
import cloud.drakon.ktdiscord.interaction.InteractionResponse
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse
import java.math.BigInteger
import java.security.KeyFactory
import java.security.Signature
import java.security.spec.EdECPoint
import java.security.spec.EdECPublicKeySpec
import java.security.spec.NamedParameterSpec
import kotlin.experimental.and
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Handler : RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {
    private val publicKey = System.getenv("DISCORD_PUBLIC_KEY")
        .hexToByteArray()
        .apply {
            this[size - 1] = this[size - 1].and(127)
        }.reversedArray()

    private val xOdd = publicKey[publicKey.size - 1].toInt().and(255).shr(7) == 1
    private val y = BigInteger(1, publicKey)

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    private val lambdaClient = LambdaClient {
        region = "eu-west-2"
    }

    override fun handleRequest(
        input: APIGatewayV2HTTPEvent,
        context: Context,
    ): APIGatewayV2HTTPResponse = runBlocking {
        val response = APIGatewayV2HTTPResponse()

        // https://stackoverflow.com/questions/65780235/ed25519-in-jdk-15-parse-public-key-from-byte-array-and-verify
        // https://github.com/myosyn/DiscordInteraKTions/blob/cd22595534f58fa742ad410545cc6c85739df958/requests-verifier/src/main/kotlin/net/perfectdreams/discordinteraktions/verifier/InteractionRequestVerifier.kt
        if (
            Signature.getInstance("Ed25519").apply {
                initVerify(
                    KeyFactory.getInstance("Ed25519").generatePublic(
                        EdECPublicKeySpec(
                            NamedParameterSpec.ED25519,
                            EdECPoint(xOdd, y)
                        )
                    )
                )

                update((input.headers["x-signature-timestamp"] + input.body).toByteArray())
            }.verify(input.headers["x-signature-ed25519"]!!.hexToByteArray())
        ) {
            when (json.decodeFromString<Interaction>(input.body).type) {
                1.toByte() -> {
                    context.logger.log("Pong")
                    response.body = json.encodeToString(InteractionResponse(type = 1))
                }

                else -> {
                    launch {
                        lambdaClient.invoke(InvokeRequest {
                            functionName = "DynamisBot_interact"
                            invocationType = InvocationType.Event
                            payload = input.body.toByteArray()
                        })
                    }

                    context.logger.log("Deferring channel message")
                    response.body = json.encodeToString(InteractionResponse(type = 5))
                }
            }

            response.statusCode = 200
            response.headers = mapOf("Content-Type" to "application/json")
        } else {
            context.logger.log("Invalid request signature")
            response.statusCode = 401
        }

        return@runBlocking response
    }
}
