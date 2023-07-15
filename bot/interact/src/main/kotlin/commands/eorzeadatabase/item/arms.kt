package cloud.drakon.cerberusbot.interact.commands.eorzeadatabase.item

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

suspend fun arms(item: JsonObject, language: String, lodestone: String) =
    coroutineScope {
        val bonuses = mutableListOf<String>()

        for (i in item["Stats"]!!.jsonObject.keys) {
            val key = Localisation.bonuses.getValue(i).getValue(language)

            val bonus = item["Stats"]!!.jsonObject[i]!!
            val value = bonus.jsonObject["NQ"]!!.jsonPrimitive.int
            val valueHq = bonus.jsonObject["HQ"]?.jsonPrimitive?.int

            if (valueHq != null) {
                bonuses.add(
                    "$key +$value / +$valueHq <:hq:916051971063054406>"
                )
            } else {
                bonuses.add(
                    "$key +$value"
                )
            }
        }

        val damageType: String
        val nqDamage: Int
        when (item["ClassJobUse"]!!.jsonObject["ClassJobCategory"]!!.jsonObject["ID"]!!.jsonPrimitive.int) {
            30 -> {
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

        val damage = if (hqDamage != null) {
            "$nqDamage / $hqDamage <:hq:916051971063054406>"
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

        val autoAttack = if (hqAutoAttack != null) {
            "$nqAutoAttack / $hqAutoAttack <:hq:916051971063054406>"
        } else {
            nqAutoAttack.toString()
        }

        val classJob = """
            ${item["ClassJobCategory"]!!.jsonObject["Name"]!!.jsonPrimitive.content}
            ${Localisation.level.getValue(language)} ${item["LevelEquip"]!!.jsonPrimitive.content}
        """.trimIndent()

        return@coroutineScope Embed(
            title = item["Name"]!!.jsonPrimitive.content,
            description = item["ItemUICategory"]!!.jsonObject["Name"]!!.jsonPrimitive.content,
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
                    value = bonuses.joinToString("\n"),
                    inline = true
                )
            )
        )
    }
