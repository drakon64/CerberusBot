package cloud.drakon.dynamisbot.eorzeadatabase.item

import cloud.drakon.ktdiscord.channel.embed.EmbedField
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class Materia(
    @SerialName("Name") override val name: String,
    @SerialName("Description") override val description: String?,
    @SerialName("IconHD") override val iconHd: String,
    @SerialName("ItemUICategory") override val itemUiCategory: Item.ItemUICategory,

    @SerialName("Materia") val materiaParam: MateriaParam,
    @SerialName("LevelItem") val levelItem: Short,
) : Item {
    @Serializable
    class MateriaParam(
        @SerialName("BaseParam") val baseParam: BaseParam,
        @SerialName("Value") val value: Byte,
    ) {
        @Serializable
        class BaseParam(
            @SerialName("Name_en") val en: String,
            @SerialName("Name_ja") val ja: String,
            @SerialName("Name_de") val de: String,
            @SerialName("Name_fr") val fr: String,
        )
    }

    override suspend fun createEmbedFields(language: String): Array<EmbedField> {
        val effects = when (language) {
            "en" -> this.materiaParam.baseParam.en
            "ja" -> this.materiaParam.baseParam.ja
            "de" -> this.materiaParam.baseParam.de
            "fr" -> this.materiaParam.baseParam.fr
            else -> throw Throwable("Unknown language: `$language`")
        }

        return arrayOf(
            EmbedField(
                name = Localisation.effects.getValue(language),
                value = "$effects +${this.materiaParam.value}",
                inline = true
            ), EmbedField(
                name = Localisation.requirements.getValue(language),
                value = "${
                    Localisation.baseItemLevel.getValue("baseItem").getValue(language)
                } ${
                    Localisation.baseItemLevel.getValue("itemLevel").getValue(language)
                } $levelItem" + if (language == "ja") "～" else "",
                inline = true
            )
        )
    }
}
