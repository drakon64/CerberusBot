package cloud.drakon.dynamisbot.eorzeadatabase.item

import cloud.drakon.ktdiscord.channel.embed.EmbedField
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable class Shield(
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

    @SerialName("Block") val block: Int,
    @SerialName("BlockRate") val blockRate: Int,

    @SerialName("BaseParamValueSpecial0") val baseParamValueSpecial0: Byte? = null,
    @SerialName("BaseParamValueSpecial1") val baseParamValueSpecial1: Byte? = null
): ArmorAccessoriesShield {
    override suspend fun createEmbedFields(language: String): Array<EmbedField> =
        coroutineScope {
            val stats = getStats(this@Shield, language)

            val classJob = """
                ${this@Shield.classJobCategory.name}
                ${Localisation.level.getValue(language)} ${this@Shield.levelEquip}
            """.trimIndent()

            val nqBlock = this@Shield.block
            val nqBlockRate = this@Shield.blockRate

            val hqBlock = if (this@Shield.baseParamValueSpecial0 != null) {
                nqBlock + this@Shield.baseParamValueSpecial0
            } else {
                null
            }

            val hqBlockRate = if (this@Shield.baseParamValueSpecial1 != null) {
                nqBlockRate + this@Shield.baseParamValueSpecial1
            } else {
                null
            }

            val block = if (hqBlock != null && hqBlock != nqBlock) {
                "$nqBlock / $hqBlock HQ"
            } else {
                "$nqBlock"
            }

            val blockRate = if (hqBlockRate != null && hqBlockRate != nqBlockRate) {
                "$nqBlockRate / $hqBlockRate HQ"
            } else {
                "$nqBlockRate"
            }

            return@coroutineScope arrayOf(
                EmbedField(
                    name = Localisation.itemLevel.getValue(language),
                    value = this@Shield.levelItem,
                ),
                EmbedField(
                    name = Localisation.block.getValue("Strength").getValue(language),
                    value = block,
                    inline = true
                ), EmbedField(
                    name = Localisation.block.getValue("Rate").getValue(language),
                    value = blockRate,
                    inline = true
                ),
                EmbedField(
                    name = "Class/Job", value = classJob
                ), EmbedField(
                    name = Localisation.bonuses.getValue("Bonuses").getValue(language),
                    value = stats!!.joinToString("\n"),
                )
            )
        }
}
