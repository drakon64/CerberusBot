package cloud.drakon.tempestbot.interact.commands

import aws.sdk.kotlin.services.translate.TranslateClient
import aws.sdk.kotlin.services.translate.translateText
import cloud.drakon.tempest.interaction.Interaction
import cloud.drakon.tempest.interaction.applicationcommand.ApplicationCommandData
import cloud.drakon.tempest.webbook.EditWebhookMessage
import cloud.drakon.tempestbot.interact.Handler

suspend fun translate(event: Interaction<ApplicationCommandData>) {
    lateinit var message: String
    lateinit var to: String
    var from: String? = null

    for (i in event.data !!.options !!) {
        when (i.name) {
            "message" -> message = i.value !!
            "to" -> to = i.value !!
            "from" -> from = i.value !!
        }
    }

    val translation: String = when {
        from != null -> TranslateClient.invoke { region = Handler.region }
            .translateText {
                text = message
                targetLanguageCode = to
                sourceLanguageCode = from
            }.translatedText !!

        else -> TranslateClient.invoke { }.translateText {
            text = message
            targetLanguageCode = to
        }.translatedText !!
    }

    Handler.tempestClient.editOriginalInteractionResponse(
        EditWebhookMessage(content = translation), event.token
    )
}
