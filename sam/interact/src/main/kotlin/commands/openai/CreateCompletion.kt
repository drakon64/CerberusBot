package cloud.drakon.tempestbot.interact.commands.openai

import cloud.drakon.ktdiscord.interaction.Interaction
import cloud.drakon.ktdiscord.interaction.applicationcommand.ApplicationCommandData
import cloud.drakon.ktdiscord.webhook.EditWebhookMessage
import cloud.drakon.tempestbot.interact.Handler
import cloud.drakon.tempestbot.interact.api.openai.OpenAI
import cloud.drakon.tempestbot.interact.api.openai.completion.CompletionRequest

suspend fun createCompletion(event: Interaction<ApplicationCommandData>) {
    lateinit var prompt: String

    for (i in event.data !!.options !!) {
        when (i.name) {
            "prompt" -> prompt = i.value !!
        }
    }

    val createdCompletion = OpenAI(System.getenv("OPENAI_API_KEY")).createCompletion(
        CompletionRequest("gpt-3.5-turbo", prompt)
    ).choices[0]["text"]

    Handler.ktDiscordClient.editOriginalInteractionResponse(
        EditWebhookMessage(content = createdCompletion), event.token
    )
}
