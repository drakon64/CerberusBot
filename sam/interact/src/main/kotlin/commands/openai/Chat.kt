package cloud.drakon.tempestbot.interact.commands.openai

import cloud.drakon.ktdiscord.interaction.Interaction
import cloud.drakon.ktdiscord.interaction.applicationcommand.ApplicationCommandData
import cloud.drakon.ktdiscord.webhook.EditWebhookMessage
import cloud.drakon.tempestbot.interact.Handler
import cloud.drakon.tempestbot.interact.api.openai.OpenAI
import cloud.drakon.tempestbot.interact.api.openai.chat.ChatRequest
import cloud.drakon.tempestbot.interact.api.openai.chat.Message
import cloud.drakon.tempestbot.interact.api.openai.chat.Messages
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Projections

suspend fun chat(event: Interaction<ApplicationCommandData>) {
    lateinit var message: String
    var assistant: String? = null

    for (i in event.data !!.options !!) {
        when (i.name) {
            "message" -> message = i.value !!
            "assistant" -> assistant = i.value !!
        }
    }

    val messages: Messages? = if (assistant != null) {
        Handler.json.decodeFromString(
            Messages.serializer(), Handler.mongoDatabase.getCollection("openai").find(
                Filters.and(
                    Filters.eq("command", "chat"), Filters.eq("assistant", assistant)
                )
            ).projection(
                Projections.fields(
                    Projections.include("messages"), Projections.excludeId()
                )
            ).first() !!.toJson()
        )
    } else {
        null
    }

    val chatMessage: MutableList<Message> =
        messages?.messages?.toMutableList() ?: mutableListOf()
    chatMessage.add(Message("user", message))

    Handler.ktDiscordClient.editOriginalInteractionResponse(
        EditWebhookMessage(
            content = OpenAI(System.getenv("OPENAI_API_KEY")).createChatCompletion(
                ChatRequest(
                    "gpt-3.5-turbo", chatMessage.toTypedArray(), temperature = 0.2
                )
            ).choices[0].message.content
        ), event.token
    )
}
