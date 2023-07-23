package cloud.drakon.dynamisbot.eorzeadatabase.item

import cloud.drakon.ktdiscord.channel.embed.EmbedField
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable class GenericItem(
    @SerialName("Name") override val name: String,
    @SerialName("Description") override val description: String?,
    @SerialName("IconHD") override val iconHd: String,
    @SerialName("ItemUICategory") override val itemUiCategory: Item.ItemUICategory
): Item {
    override suspend fun createEmbedFields(language: String): Array<EmbedField>? =
        coroutineScope {
            return@coroutineScope null
        }
}
