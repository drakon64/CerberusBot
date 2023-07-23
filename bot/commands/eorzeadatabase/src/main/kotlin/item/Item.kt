package cloud.drakon.dynamisbot.eorzeadatabase.item

import cloud.drakon.ktdiscord.channel.embed.Embed
import cloud.drakon.ktdiscord.channel.embed.EmbedField
import cloud.drakon.ktdiscord.channel.embed.EmbedThumbnail
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable sealed interface Item {
    @SerialName("Name") val name: String
    @SerialName("Description") val description: String?
    @SerialName("IconHD") val iconHd: String

    suspend fun createEmbedFields(language: String): Array<EmbedField>?

    suspend fun createEmbed(fields: Array<EmbedField>? = null) = coroutineScope {
        return@coroutineScope Embed(
            title = name,
            description = description,
            url = "https://eu.finalfantasyxiv.com/lodestone/playguide/db/search/?q=${
                name.replace(
                    " ", "+"
                )
            }&db_search_category=item",
            thumbnail = EmbedThumbnail(url = "https://xivapi.com${iconHd}"),
            fields = fields
        )
    }
}
