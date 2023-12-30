package cloud.drakon.ktdiscord.interaction

import cloud.drakon.ktdiscord.guild.Member
import cloud.drakon.ktdiscord.interaction.applicationcommand.ApplicationCommandData
import cloud.drakon.ktdiscord.Locale
import cloud.drakon.ktdiscord.user.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// https://discord.com/developers/docs/interactions/receiving-and-responding#interaction-object-interaction-structure
@Serializable
data class Interaction(
    @SerialName("application_id") val applicationId: Long,
    val type: Byte,
    val data: ApplicationCommandData? = null,
    @SerialName("guild_id") val guildId: Long? = null,
    val member: Member? = null,
    val user: User? = null,
    val token: String,
    val locale: Locale,
)
