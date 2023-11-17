package cloud.drakon.dynamisbot.eorzeadatabase.item

import cloud.drakon.ktdiscord.channel.embed.EmbedField
import kotlin.math.floor
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable class ArmsTools(
    @SerialName("Name") override val name: String,
    @SerialName("Description") override val description: String? = null,
    @SerialName("ItemUICategory") override val itemUiCategory: Item.ItemUICategory,
    @SerialName("IconHD") override val iconHd: String,
    @SerialName("Stats") override val stats: Map<String, Map<String, Int>>,

    @SerialName("ClassJobCategory") val classJobCategory: ClassJobCategory,
    @SerialName("LevelEquip") val levelEquip: String,
    @SerialName("ClassJobUse") val classJobUse: ClassJobUse? = null,
    @SerialName("DamagePhys") val damagePhys: Int,
    @SerialName("DamageMag") val damageMag: Int,
    @SerialName("DelayMs") val delayMs: Double,
    @SerialName("LevelItem") val levelItem: String,

    @SerialName("BaseParamValueSpecial0") val baseParamValueSpecial0: Byte? = null,
): StatsItem {
    @Serializable class ClassJobCategory(@SerialName("Name") val name: String)
    @Serializable class ClassJobUse(
        @SerialName("ClassJobCategory") val classJobCategory: ClassJobCategory? = null
    ) {
        @Serializable class ClassJobCategory(@SerialName("ID") val id: Int? = null)
    }

    override suspend fun createEmbedFields(language: String) = coroutineScope {
        val stats = getStats(this@ArmsTools, language)

        val classJob = """
            ${this@ArmsTools.classJobCategory.name}
            ${Localisation.level.getValue(language)} ${this@ArmsTools.levelEquip}
        """.trimIndent()

        val embeds: Array<EmbedField>

        val classJobCategoryId = this@ArmsTools.classJobUse?.classJobCategory?.id

        if (classJobCategoryId != null) {
            val damageType: String
            val nqDamage: Int

            when (classJobCategoryId) {
                30, 32, 33 -> {
                    damageType =
                        Localisation.damageType.getValue("Physical Damage")
                            .getValue(language)

                    nqDamage = this@ArmsTools.damagePhys
                }

                31 -> {
                    damageType =
                        Localisation.damageType.getValue("Magic Damage")
                            .getValue(language)

                    nqDamage = this@ArmsTools.damageMag
                }

                else -> throw Throwable("Unknown class/job category: $this")
            }

            val hqDamage = if (this@ArmsTools.baseParamValueSpecial0 != null) {
                nqDamage + this@ArmsTools.baseParamValueSpecial0
            } else {
                null
            }

            val damage = if (hqDamage != null && hqDamage != nqDamage) {
                "$nqDamage / $hqDamage HQ"
            } else {
                nqDamage.toString()
            }

            val delay = this@ArmsTools.delayMs / 1000

            val nqAutoAttack = (floor(((delay / 3) * nqDamage) * 100) / 100).toString()

            val hqAutoAttack = if (hqDamage != null) {
                (floor(((delay / 3) * hqDamage) * 100) / 100).toString()
            } else {
                null
            }

            val autoAttack = if (hqAutoAttack != null && hqAutoAttack != nqAutoAttack) {
                "$nqAutoAttack / $hqAutoAttack HQ"
            } else {
                nqAutoAttack
            }

            embeds = arrayOf(
                EmbedField(
                    name = Localisation.itemLevel.getValue(language),
                    value = this@ArmsTools.levelItem,
                ), EmbedField(
                    name = damageType, value = damage, inline = true
                ), EmbedField(
                    name = Localisation.autoAttack.getValue(language),
                    value = autoAttack,
                    inline = true
                ), EmbedField(
                    name = Localisation.delay.getValue(language),
                    value = delay.toString(),
                    inline = true
                ), EmbedField(
                    name = "Class/Job", value = classJob
                ), EmbedField(
                    name = Localisation.bonuses.getValue("Bonuses").getValue(language),
                    value = stats!!.joinToString("\n")
                )
            )
        } else {
            embeds = arrayOf(
                EmbedField(
                    name = Localisation.itemLevel.getValue(language),
                    value = this@ArmsTools.levelItem,
                ), EmbedField(
                    name = "Class/Job", value = classJob, inline = true
                ), EmbedField(
                    name = Localisation.bonuses.getValue("Bonuses").getValue(language),
                    value = stats!!.joinToString("\n"),
                    inline = true
                )
            )
        }

        return@coroutineScope embeds
    }
}
