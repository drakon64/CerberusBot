package cloud.drakon.dynamisbot.eorzeadatabase.item

import cloud.drakon.ktdiscord.channel.embed.EmbedField
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable class Armor(
    @SerialName("Name") override val name: String,
    @SerialName("Description") override val description: String? = null,

    @SerialName("ClassJobCategory")
    override val classJobCategory: ArmorAccessories.ClassJobCategory,

    @SerialName("LevelEquip") override val levelEquip: String,

    @SerialName("ItemUICategory")
    override val itemUiCategory: StatsItem.ItemUICategory,

    @SerialName("IconHD") override val iconHd: String,

    @SerialName("Stats") override val stats: Map<String, Map<String, Int>>,

    @SerialName("EquipSlotCategoryTargetID") val equipSlotCategoryTargetId: Int,
    @SerialName("LevelItem") val levelItem: String,

    @SerialName("Block") val block: Int,
    @SerialName("BlockRate") val blockRate: Int,

    @SerialName("DefensePhys") val defensePhys: Int,
    @SerialName("DefenseMag") val defenseMag: Int,

    @SerialName("BaseParamValueSpecial0") val baseParamValueSpecial0: Byte? = null,
    @SerialName("BaseParamValueSpecial1") val baseParamValueSpecial1: Byte? = null
): ArmorAccessories {
    override suspend fun createEmbedFields(language: String): Array<EmbedField> =
        coroutineScope {
            val stats = getStats(this@Armor, language)

            val classJob = """
                ${this@Armor.classJobCategory.name}
                ${Localisation.level.getValue(language)} ${this@Armor.levelEquip}
            """.trimIndent()

            val embedFieldOne: EmbedField
            val embedFieldTwo: EmbedField

            if (this@Armor.equipSlotCategoryTargetId == 2) {
                val nqBlock = this@Armor.block
                val nqBlockRate = this@Armor.blockRate

                val hqBlock = if (this@Armor.baseParamValueSpecial0 != null) {
                    nqBlock + this@Armor.baseParamValueSpecial0
                } else {
                    null
                }

                val hqBlockRate = if (this@Armor.baseParamValueSpecial1 != null) {
                    nqBlockRate + this@Armor.baseParamValueSpecial1
                } else {
                    null
                }

                val block = if (hqBlock != null && hqBlock != nqBlock) {
                    "$nqBlock / $hqBlock <:hqlight:673889304359206923>"
                } else {
                    "$nqBlock"
                }

                val blockRate = if (hqBlockRate != null && hqBlockRate != nqBlockRate) {
                    "$nqBlockRate / $hqBlockRate <:hqlight:673889304359206923>"
                } else {
                    "$nqBlockRate"
                }

                embedFieldOne = EmbedField(
                    name = Localisation.block.getValue("Strength").getValue(language),
                    value = block,
                    inline = true
                )

                embedFieldTwo = EmbedField(
                    name = Localisation.block.getValue("Rate").getValue(language),
                    value = blockRate,
                    inline = true
                )
            } else {
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

                embedFieldOne = EmbedField(
                    name = Localisation.defense.getValue("Defense").getValue(language),
                    value = physicalDefense,
                    inline = true
                )

                embedFieldTwo = EmbedField(
                    name = Localisation.defense.getValue("Magic Defense")
                        .getValue(language),
                    value = magicDefense,
                    inline = true
                )
            }

            return@coroutineScope arrayOf(
                EmbedField(
                    name = Localisation.itemLevel.getValue(language),
                    value = this@Armor.levelItem,
                ),
                embedFieldOne, embedFieldTwo,
                EmbedField(
                    name = "Class/Job", value = classJob
                ), EmbedField(
                    name = Localisation.bonuses.getValue("Bonuses").getValue(language),
                    value = stats.joinToString("\n"),
                )
            )
        }
}
