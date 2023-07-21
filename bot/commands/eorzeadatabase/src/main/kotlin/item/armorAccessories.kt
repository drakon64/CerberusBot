package cloud.drakon.dynamisbot.eorzeadatabase.item

import cloud.drakon.ktdiscord.channel.embed.Embed
import cloud.drakon.ktdiscord.channel.embed.EmbedField
import cloud.drakon.ktdiscord.channel.embed.EmbedThumbnail
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

suspend fun armorAccessories(item: JsonObject, language: String, lodestone: String) =
    coroutineScope {
        val stats = getStats(item, language)

        val embedFieldOne: EmbedField
        val embedFieldTwo: EmbedField

        if (item["EquipSlotCategoryTargetID"]!!.jsonPrimitive.int == 2) {
            val nqBlock = item["Block"]!!.jsonPrimitive.int
            val nqBlockRate = item["BlockRate"]!!.jsonPrimitive.int

            val hqBlock =
                if (item["BaseParamValueSpecial0"]?.jsonPrimitive?.int != null) {
                    nqBlock + item["BaseParamValueSpecial0"]!!.jsonPrimitive.int
                } else {
                    null
                }

            val hqBlockRate =
                if (item["BaseParamValueSpecial1"]?.jsonPrimitive?.int != null) {
                    nqBlockRate + item["BaseParamValueSpecial1"]!!.jsonPrimitive.int
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
            val nqPhysicalDefense = item["DefensePhys"]!!.jsonPrimitive.int
            val nqMagicDefense = item["DefenseMag"]!!.jsonPrimitive.int

            val hqPhysicalDefense =
                if (item["BaseParamValueSpecial0"]?.jsonPrimitive?.int != null) {
                    nqPhysicalDefense + item["BaseParamValueSpecial0"]!!.jsonPrimitive.int
                } else {
                    null
                }

            val hqMagicDefense =
                if (item["BaseParamValueSpecial1"]?.jsonPrimitive?.int != null) {
                    nqMagicDefense + item["BaseParamValueSpecial1"]!!.jsonPrimitive.int
                } else {
                    null
                }

            val physicalDefense =
                if (hqPhysicalDefense != null && hqPhysicalDefense != nqPhysicalDefense) {
                    "$nqPhysicalDefense / $hqPhysicalDefense <:hqlight:673889304359206923>"
                } else {
                    "$nqPhysicalDefense"
                }

            val magicDefense =
                if (hqMagicDefense != null && hqMagicDefense != nqMagicDefense) {
                    "$nqMagicDefense / $hqMagicDefense <:hqlight:673889304359206923>"
                } else {
                    "$nqMagicDefense"
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
            }&db_search_category=item",
            thumbnail = EmbedThumbnail(url = "https://xivapi.com${item["IconHD"]!!.jsonPrimitive.content}"),
            fields = arrayOf(
                EmbedField(
                    name = Localisation.itemLevel.getValue(language),
                    value = item["LevelItem"]!!.jsonPrimitive.content,
                ), embedFieldOne, embedFieldTwo,
                EmbedField(
                    name = "Class/Job", value = classJob
                ), EmbedField(
                    name = Localisation.bonuses.getValue("Bonuses").getValue(language),
                    value = stats.joinToString("\n"),
                )
            )
        )
    }
