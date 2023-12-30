package cloud.drakon.ktdiscord.guild

import cloud.drakon.ktdiscord.user.User
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class Member(val user: User? = null)
