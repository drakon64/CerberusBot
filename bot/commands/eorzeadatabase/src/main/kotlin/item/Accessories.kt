package cloud.drakon.dynamisbot.eorzeadatabase.item

import cloud.drakon.ktdiscord.channel.embed.EmbedField
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable class Accessories(
    @SerialName("Name") override val name: String,
    @SerialName("Description") override val description: String? = null,

    @SerialName("ClassJobCategory")
    override val classJobCategory: ArmorAccessories.ClassJobCategory,

    @SerialName("ItemUICategory")
    override val itemUiCategory: StatsItem.ItemUICategory,

    @SerialName("IconHD") override val iconHd: String,
    @SerialName("Stats") override val stats: Map<String, Map<String, Int>>,
    @SerialName("LevelEquip") override val levelEquip: String,

    @SerialName("LevelItem") val levelItem: String,
    @SerialName("DefensePhys") val defensePhys: Int,
    @SerialName("DefenseMag") val defenseMag: Int,

    @SerialName("BaseParamValueSpecial0") val baseParamValueSpecial0: Byte? = null,
    @SerialName("BaseParamValueSpecial1") val baseParamValueSpecial1: Byte? = null
): ArmorAccessories {
    override suspend fun createEmbedFields(language: String) = coroutineScope {
        val stats = getStats(this@Accessories, language)

        val nqPhysicalDefense = this@Accessories.defensePhys
        val nqMagicDefense = this@Accessories.defenseMag

        val hqPhysicalDefense = if (this@Accessories.baseParamValueSpecial0 != null) {
            nqPhysicalDefense + this@Accessories.baseParamValueSpecial0
        } else {
            null
        }

        val hqMagicDefense = if (this@Accessories.baseParamValueSpecial1 != null) {
            nqMagicDefense + this@Accessories.baseParamValueSpecial1
        } else {
            null
        }

        val physicalDefense =
            if (hqPhysicalDefense != null && hqPhysicalDefense != nqPhysicalDefense) {
                "$nqPhysicalDefense / $hqPhysicalDefense <:hqlight:673889304359206923>"
            } else {
                nqPhysicalDefense.toString()
            }

        val magicDefense =
            if (hqMagicDefense != null && hqMagicDefense != nqMagicDefense) {
                "$nqMagicDefense / $hqMagicDefense <:hqlight:673889304359206923>"
            } else {
                nqMagicDefense.toString()
            }

        val classJob = """
            ${this@Accessories.classJobCategory.name}
            ${Localisation.level.getValue(language)} ${this@Accessories.levelEquip}
        """.trimIndent()

        return@coroutineScope arrayOf(
            EmbedField(
                name = Localisation.itemLevel.getValue(language),
                value = this@Accessories.levelItem,
            ),
            EmbedField(
                name = Localisation.defense.getValue("Defense").getValue(language),
                value = physicalDefense,
                inline = true
            ),
            EmbedField(
                name = Localisation.defense.getValue("Magic Defense")
                    .getValue(language),
                value = magicDefense,
                inline = true
            ),
            EmbedField(
                name = "Class/Job", value = classJob
            ), EmbedField(
                name = Localisation.bonuses.getValue("Bonuses").getValue(language),
                value = stats.joinToString("\n"),
            )
        )
    }
}
