package cloud.drakon.dynamisbot.eorzeadatabase.item

import cloud.drakon.dynamisbot.eorzeadatabase.cleanDescription
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

    @SerialName("ItemUICategory")
    val itemUiCategory: ItemUICategory

    @Serializable class ItemUICategory(@SerialName("Name") val name: String)

    suspend fun createEmbedFields(language: String): Array<EmbedField>?

    suspend fun createEmbed(
        fields: Array<EmbedField>?,
        lodestone: String
    ) = coroutineScope {
        val description = if (this@Item.description.isNullOrBlank()) {
            cleanDescription(this@Item.itemUiCategory.name)
        } else {
            cleanDescription(
                this@Item.itemUiCategory.name
                + "\n\n"
                + this@Item.description
            )
        }

        return@coroutineScope Embed(
            title = name,
            description = description,
            url = "https://$lodestone.finalfantasyxiv.com/lodestone/playguide/db/search/?q=${
                name.replace(
                    " ", "+"
                )
            }&db_search_category=item",
            thumbnail = EmbedThumbnail(url = "https://xivapi.com${iconHd}"),
            fields = fields
        )
    }
}
