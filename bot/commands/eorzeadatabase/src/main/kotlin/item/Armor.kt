package cloud.drakon.dynamisbot.eorzeadatabase.item

import cloud.drakon.ktdiscord.channel.embed.EmbedField
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable class Armor(
    @SerialName("Name") override val name: String,
    @SerialName("Description") override val description: String? = null,

    @SerialName("ClassJobCategory")
    override val classJobCategory: ArmorAccessoriesShield.ClassJobCategory,

    @SerialName("LevelEquip") override val levelEquip: String,

    @SerialName("ItemUICategory")
    override val itemUiCategory: Item.ItemUICategory,

    @SerialName("IconHD") override val iconHd: String,

    @SerialName("Stats") override val stats: Map<String, Map<String, Int>>,

    @SerialName("LevelItem") val levelItem: String,

    @SerialName("DefensePhys") val defensePhys: Int,
    @SerialName("DefenseMag") val defenseMag: Int,

    @SerialName("BaseParamValueSpecial0") val baseParamValueSpecial0: Byte? = null,
    @SerialName("BaseParamValueSpecial1") val baseParamValueSpecial1: Byte? = null,
) : ArmorAccessoriesShield {
    override suspend fun createEmbedFields(language: String) =
        coroutineScope {
            val stats = getStats(this@Armor, language)

            val classJob = """
                ${this@Armor.classJobCategory.name}
                ${Localisation.level.getValue(language)} ${this@Armor.levelEquip}
            """.trimIndent()

            val hqPhysicalDefense = if (this@Armor.baseParamValueSpecial0 != null) {
                this@Armor.defensePhys + this@Armor.baseParamValueSpecial0
            } else {
                null
            }

            val hqMagicDefense = if (this@Armor.baseParamValueSpecial1 != null) {
                this@Armor.defenseMag + this@Armor.baseParamValueSpecial1
            } else {
                null
            }

            val physicalDefense =
                if (hqPhysicalDefense != null && hqPhysicalDefense != this@Armor.defensePhys) {
                    "${this@Armor.defensePhys} / $hqPhysicalDefense <:hqlight:673889304359206923>"
                } else {
                    this@Armor.defensePhys.toString()
                }

            val magicDefense =
                if (hqMagicDefense != null && hqMagicDefense != this@Armor.defenseMag) {
                    "${this@Armor.defenseMag} / $hqMagicDefense <:hqlight:673889304359206923>"
                } else {
                    this@Armor.defenseMag.toString()
                }

            buildList {
                this.add(
                    EmbedField(
                        name = Localisation.itemLevel.getValue(language),
                        value = this@Armor.levelItem,
                    )
                )

                this.add(
                    EmbedField(
                        name = Localisation.defense.getValue("Defense")
                            .getValue(language),
                        value = physicalDefense,
                        inline = true
                    )
                )

                this.add(
                    EmbedField(
                        name = Localisation.defense.getValue("Magic Defense")
                            .getValue(language),
                        value = magicDefense,
                        inline = true
                    )
                )

                this.add(
                    EmbedField(
                        name = "Class/Job", value = classJob
                    )
                )

                if (stats != null) this.add(
                    EmbedField(
                        name = Localisation.bonuses.getValue("Bonuses")
                            .getValue(language),
                        value = stats.joinToString("\n"),
                    )
                )
            }.toTypedArray()
        }
}
