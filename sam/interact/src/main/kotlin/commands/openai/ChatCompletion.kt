package cloud.drakon.tempestbot.interact.commands.openai

import cloud.drakon.ktdiscord.interaction.Interaction
import cloud.drakon.ktdiscord.interaction.applicationcommand.ApplicationCommandData
import cloud.drakon.ktdiscord.webhook.EditWebhookMessage
import cloud.drakon.tempestbot.interact.Handler
import cloud.drakon.tempestbot.interact.api.openai.OpenAI
import cloud.drakon.tempestbot.interact.api.openai.chat.ChatRequest
import cloud.drakon.tempestbot.interact.api.openai.chat.Message

suspend fun createCompletion(event: Interaction<ApplicationCommandData>) {
    lateinit var message: String

    for (i in event.data !!.options !!) {
        when (i.name) {
            "message" -> message = i.value !!
        }
    }

    Handler.ktDiscordClient.editOriginalInteractionResponse(
        EditWebhookMessage(
            content = OpenAI(System.getenv("OPENAI_API_KEY")).createChatCompletion(
                ChatRequest(
                    "gpt-3.5-turbo", arrayOf(
                        Message("system", System.getenv("OPENAI_CHAT_SYSTEM_MESSAGE")),
                        Message("user", message)
                    ), temperature = 0.5
                )
            ).choices[0].message.content
        ), event.token
    )
}
