package cloud.drakon.tempestbot.interact.commands.citations

import cloud.drakon.ktdiscord.channel.Attachment
import kotlinx.serialization.Serializable

@Serializable class Citations(
    val messages: Array<Citation>? = null,
)

@Serializable class Citation(
    val attachments: Array<Attachment>? = null,
    val content: String? = null,
)
