package cloud.drakon.dynamisbot.eorzeadatabase.item

import cloud.drakon.dynamisbot.eorzeadatabase.cleanDescription
import cloud.drakon.ktdiscord.channel.embed.Embed
import cloud.drakon.ktdiscord.channel.embed.EmbedField
import cloud.drakon.ktdiscord.channel.embed.EmbedThumbnail
import java.math.BigDecimal
import java.math.RoundingMode
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

suspend fun armsTools(item: JsonObject, language: String, lodestone: String) =
    coroutineScope {
        val stats = getStats(item, language)

        val description =
            cleanDescription(item["Description"]!!.jsonPrimitive.content.ifBlank {
                item["ItemUICategory"]!!.jsonObject["Name"]!!.jsonPrimitive.content
            })

        val damageType: String
        val nqDamage: Int
        when (item["ClassJobUse"]!!.jsonObject["ClassJobCategory"]!!.jsonObject["ID"]!!.jsonPrimitive.int) {
            30, 32, 33 -> {
                damageType =
                    Localisation.damageType.getValue("Physical Damage")
                        .getValue(language)

                nqDamage = item["DamagePhys"]!!.jsonPrimitive.int
            }

            31 -> {
                damageType =
                    Localisation.damageType.getValue("Magic Damage")
                        .getValue(language)

                nqDamage = item["DamageMag"]!!.jsonPrimitive.int
            }

            else -> throw Throwable("Unknown class/job category: $this")
        }

        val hqDamage =
            if (item["BaseParamValueSpecial0"]?.jsonPrimitive?.int != null) {
                nqDamage + item["BaseParamValueSpecial0"]!!.jsonPrimitive.int
            } else {
                null
            }

        val damage = if (hqDamage != null && hqDamage != nqDamage) {
            "$nqDamage / $hqDamage <:hqlight:673889304359206923>"
        } else {
            nqDamage.toString()
        }

        val delay = ((item["DelayMs"]!!.jsonPrimitive.int).toDouble() / 1000)

        val nqAutoAttack =
            BigDecimal.valueOf((delay / 3) * nqDamage)
                .setScale(2, RoundingMode.DOWN)

        val hqAutoAttack = if (hqDamage != null) {
            BigDecimal.valueOf((delay / 3) * hqDamage)
                .setScale(2, RoundingMode.DOWN)
        } else {
            null
        }

        val autoAttack = if (hqAutoAttack != null && hqAutoAttack != nqAutoAttack) {
            "$nqAutoAttack / $hqAutoAttack <:hqlight:673889304359206923>"
        } else {
            nqAutoAttack.toString()
        }

        val classJob = """
            ${item["ClassJobCategory"]!!.jsonObject["Name"]!!.jsonPrimitive.content}
            ${Localisation.level.getValue(language)} ${item["LevelEquip"]!!.jsonPrimitive.content}
        """.trimIndent()

        return@coroutineScope Embed(
            title = item["Name"]!!.jsonPrimitive.content,
            description = description,
            url = "https://$lodestone.finalfantasyxiv.com/lodestone/playguide/db/search/?q=${
                item["Name"]!!.jsonPrimitive.content.replace(
                    " ", "+"
                )
            }",
            thumbnail = EmbedThumbnail(url = "https://xivapi.com${item["IconHD"]!!.jsonPrimitive.content}"),
            fields = arrayOf(
                EmbedField(
                    name = Localisation.itemLevel.getValue(language),
                    value = item["LevelItem"]!!.jsonPrimitive.content,
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
                    name = "Class/Job", value = classJob, inline = true
                ), EmbedField(
                    name = Localisation.bonuses.getValue("Bonuses").getValue(language),
                    value = stats.joinToString("\n"),
                    inline = true
                )
            )
        )
    }
