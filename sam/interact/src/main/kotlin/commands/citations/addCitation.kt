package cloud.drakon.tempestbot.interact.commands.citations

import cloud.drakon.ktdiscord.channel.Attachment
import cloud.drakon.ktdiscord.file.File
import cloud.drakon.ktdiscord.interaction.Interaction
import cloud.drakon.ktdiscord.interaction.applicationcommand.ApplicationCommandData
import cloud.drakon.ktdiscord.webhook.EditWebhookMessage
import cloud.drakon.tempestbot.interact.Handler
import cloud.drakon.tempestbot.interact.Handler.Companion.json
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.java.Java
import io.ktor.client.request.get
import kotlinx.coroutines.delay
import kotlinx.serialization.encodeToString
import org.bson.BsonArray
import org.bson.BsonDocument
import org.bson.Document

suspend fun addCitation(event: Interaction<ApplicationCommandData>) {
    var attachments: Array<Attachment>? = null
    lateinit var message: String
    lateinit var userId: String
    val guildId = event.guildId

    when (event.data !!.type) {
        1 -> for (i in event.data !!.options !![0].options !!) {
            when (i.name) {
                "citation" -> message = i.value !!
                "user" -> userId = i.value !!
            }
        }

        3 -> {
            if (event.data !!.resolved !!.messages !!.values.first().attachments.isNotEmpty()) {
                attachments =
                    event.data !!.resolved !!.messages !!.values.first().attachments
            }

            message = event.data !!.resolved !!.messages !!.values.first().content
            userId = event.data !!.resolved !!.messages !!.values.first().author.id
        }
    }

    val document = Document()
    if (attachments == null) {
        document.append("attachments", null)
    } else {
        val array = BsonArray()
        for (i in attachments) {
            array.add(
                BsonDocument.parse(
                    json.encodeToString(
                        Attachment(
                            id = i.id,
                            filename = i.filename,
                            url = i.url,
                            proxyUrl = i.proxyUrl
                        )
                    )
                )
            )
        }
        document.append("attachments", array)
    }
    if (message.isNotEmpty()) {
        document.append("content", message)
    } else {
        document.append("content", null)
    }

    val query = mongoCollection.findOneAndUpdate(
        Filters.and(Filters.eq("user_id", userId), Filters.eq("guild_id", guildId)),
        Updates.addToSet("messages", document)
    )

    if (query != null && query.isNotEmpty()) {
        val files = ArrayList<File>()
        val webhookAttachments = ArrayList<Attachment>()
        if (attachments != null) {
            for (i in attachments) {
                files.add(
                    File(
                        i.id,
                        i.filename,
                        i.contentType !!,
                        HttpClient(Java).get(i.url !!).body() as ByteArray
                    )
                )
                webhookAttachments.add(Attachment(i.id, i.filename, i.contentType))
            }
        }
        if (message.isNotEmpty()) {
            Handler.ktDiscordClient.editOriginalInteractionResponse(
                EditWebhookMessage(
                    content = "> " + message.replace("\n", "\n> ") + "\n- <@$userId>",
                    files = if (files.isNotEmpty()) {
                        files.toTypedArray()
                    } else {
                        null
                    },
                    attachments = if (webhookAttachments.isNotEmpty()) {
                        webhookAttachments.toTypedArray()
                    } else {
                        null
                    }
                ), interactionToken = event.token
            )
        } else {
            Handler.ktDiscordClient.editOriginalInteractionResponse(
                EditWebhookMessage(
                    files = if (files.isNotEmpty()) {
                        files.toTypedArray()
                    } else {
                        null
                    }, attachments = if (webhookAttachments.isNotEmpty()) {
                        webhookAttachments.toTypedArray()
                    } else {
                        null
                    }
                ), interactionToken = event.token
            )
        }
    } else {
        Handler.ktDiscordClient.editOriginalInteractionResponse(
            EditWebhookMessage(
                content = "User has not opted-in to citations!"
            ), interactionToken = event.token
        )

        delay(5000)

        Handler.ktDiscordClient.deleteOriginalInteractionResponse(event.token)
    }
}
