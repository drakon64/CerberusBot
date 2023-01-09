package cloud.drakon.tempestbot.interact.commands.citations

import cloud.drakon.ktdiscord.channel.Attachment
import cloud.drakon.ktdiscord.file.File
import cloud.drakon.ktdiscord.interaction.Interaction
import cloud.drakon.ktdiscord.interaction.applicationcommand.ApplicationCommandData
import cloud.drakon.ktdiscord.webhook.EditWebhookMessage
import cloud.drakon.tempestbot.interact.Handler
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Projections
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.java.Java
import io.ktor.client.request.get
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

suspend fun getCitation(event: Interaction<ApplicationCommandData>) {
    lateinit var userId: String

    when (event.data !!.type) {
        1 -> for (i in event.data !!.options !![0].options !!) {
            when (i.name) {
                "user" -> userId = i.value !!
            }
        }

        2 -> userId = event.data !!.resolved !!.users !!.keys.first()
    }

    val citations = mongoCollection.find(
        Filters.and(
            Filters.eq("user_id", userId), Filters.eq("guild_id", event.guildId)
        )
    ).projection(
        Projections.fields(Projections.include("messages"), Projections.excludeId())
    ).first()

    var error = true
    val content: String
    val attachments = ArrayList<Attachment>()

    if (citations != null) {
        val messagesJson =
            Handler.json.parseToJsonElement(citations.toJson()).jsonObject["messages"] !!.jsonArray
        val messages = ArrayList<String>()

        if (messagesJson.isNotEmpty()) {
            for (i in messagesJson) {
                if (i.jsonObject["content"] !!.jsonPrimitive.content.isNotEmpty() && i.jsonObject["content"] !!.jsonPrimitive.content != "null") { // TODO: Serialise to a class so we don't have this ugly null check
                    messages.add(
                        i.jsonObject["content"] !!.jsonPrimitive.content
                    )
                }
                if (i.jsonObject["attachments"] !!.jsonArray.isNotEmpty()) {
                    for (j in i.jsonObject["attachments"] !!.jsonArray) {
                        attachments.add(
                            Json.decodeFromJsonElement(
                                Attachment.serializer(), j.jsonObject
                            )
                        )
                    }
                }
            }

            error = false
            content = if (messages.isNotEmpty()) {
                "> " + messages.random().replace("\n", "\n> ") + "\n- <@$userId>"
            } else {
                "- <@$userId>"
            }
        } else {
            content = "No citations saved for the user!"
        }
    } else {
        content = "User has not opted-in to citations!"
    }

    val files = ArrayList<File>()
    if (attachments.isNotEmpty()) {
        val httpClient = HttpClient(Java)
        for (i in attachments) {
            files.add(
                File(
                    i.id,
                    i.filename,
                    i.contentType !!,
                    httpClient.get(i.url !!).body() as ByteArray
                )
            )
        }
    }

    Handler.ktDiscordClient.editOriginalInteractionResponse(
        EditWebhookMessage(
            content = content, files = if (files.isNotEmpty()) {
                files.toTypedArray()
            } else {
                null
            }, attachments = if (attachments.isNotEmpty()) {
                attachments.toTypedArray()
            } else {
                null
            }
        ), interactionToken = event.token
    )

    if (error) {
        delay(5000)

        Handler.ktDiscordClient.deleteOriginalInteractionResponse(event.token)
    }
}
