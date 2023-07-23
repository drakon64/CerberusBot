package cloud.drakon.dynamisbot.eorzeadatabase.item

import cloud.drakon.dynamisbot.eorzeadatabase.cleanDescription
import cloud.drakon.ktdiscord.channel.embed.Embed
import cloud.drakon.ktdiscord.channel.embed.EmbedField
import cloud.drakon.ktdiscord.channel.embed.EmbedThumbnail
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable sealed interface ArmorAccessoriesShield: StatsItem {
    @SerialName("Name") override val name: String
    @SerialName("IconHD") override val iconHd: String
    @SerialName("Stats") override val stats: Map<String, Map<String, Int>>

    @SerialName("ClassJobCategory") val classJobCategory: ClassJobCategory
    @SerialName("LevelEquip") val levelEquip: String

    @Serializable class ClassJobCategory(@SerialName("Name") val name: String)

    override suspend fun createEmbed(fields: Array<EmbedField>?): Embed = coroutineScope {
        val description = if (this@ArmorAccessoriesShield.description.isNullOrBlank()) {
            cleanDescription(this@ArmorAccessoriesShield.itemUiCategory.name)
        } else {
            cleanDescription(
                this@ArmorAccessoriesShield.itemUiCategory.name
                + "\n\n"
                + this@ArmorAccessoriesShield.description
            )
        }

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
