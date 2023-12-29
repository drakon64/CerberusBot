package cloud.drakon.dynamisbot.discord.guild

import cloud.drakon.dynamisbot.discord.user.User
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class Member(val user: User? = null)
