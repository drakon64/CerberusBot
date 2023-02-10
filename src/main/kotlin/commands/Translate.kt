package cloud.drakon.tempestbot.commands

import aws.sdk.kotlin.services.translate.TranslateClient
import aws.sdk.kotlin.services.translate.translateText
import cloud.drakon.ktdiscord.interaction.Interaction
import cloud.drakon.ktdiscord.interaction.applicationcommand.ApplicationCommandData
import cloud.drakon.ktdiscord.webhook.EditWebhookMessage
import cloud.drakon.tempestbot.Handler
import com.amazonaws.services.lambda.runtime.LambdaLogger

suspend fun translate(
    event: Interaction<ApplicationCommandData>,
    logger: LambdaLogger,
) {
    logger.log("Responding to Translate command")

    lateinit var message: String
    lateinit var to: String
    var from = "auto"

    when (event.data !!.type) {
        1 -> for (i in event.data !!.options !!) {
            when (i.name) {
                "message" -> message = i.value !!
                "to" -> to = i.value !!
                "from" -> from = i.value !!
            }
        }

        3 -> {
            message = event.data !!.resolved !!.messages !!.values.first().content
            to = event.locale !!
        }

        else -> logger.log("Unknown application command type: " + event.data !!.type)
    }

    val translation = TranslateClient.invoke { region = Handler.region }.translateText {
        text = message
        targetLanguageCode = to
        sourceLanguageCode = from
    }

    val translatedMessage: String = if (from == "auto") {
        "${translation.sourceLanguageCode}: ${translation.translatedText}"
    } else translation.translatedText !!

    Handler.ktDiscordClient.editOriginalInteractionResponse(
        EditWebhookMessage(content = translatedMessage), event.token
    )
}
