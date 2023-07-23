package cloud.drakon.dynamisbot.eorzeadatabase.item

import cloud.drakon.dynamisbot.eorzeadatabase.cleanDescription
import cloud.drakon.ktdiscord.channel.embed.Embed
import cloud.drakon.ktdiscord.channel.embed.EmbedField
import cloud.drakon.ktdiscord.channel.embed.EmbedThumbnail
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable class GenericItem(
    @SerialName("Name") override val name: String,
    @SerialName("Description") override val description: String?,
    @SerialName("IconHD") override val iconHd: String
): Item {
    override suspend fun createEmbedFields(language: String): Array<EmbedField>? =
        coroutineScope {
            return@coroutineScope null
        }

    override suspend fun createEmbed(fields: Array<EmbedField>?) = coroutineScope {
        val description = if (this@GenericItem.description != null) {
            cleanDescription(this@GenericItem.description)
        } else {
            null
        }

        return@coroutineScope Embed(
            title = this@GenericItem.name,
            description = description,
            url = "https://eu.finalfantasyxiv.com/lodestone/playguide/db/search/?q=${
                this@GenericItem.name.replace(
                    " ", "+"
                )
            }&db_search_category=item",
            thumbnail = EmbedThumbnail(url = "https://xivapi.com${this@GenericItem.iconHd}")
        )
    }
}
