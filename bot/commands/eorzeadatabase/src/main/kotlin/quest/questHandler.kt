package cloud.drakon.dynamisbot.eorzeadatabase.quest

import cloud.drakon.ktdiscord.channel.embed.Embed
import cloud.drakon.ktdiscord.channel.embed.EmbedField
import cloud.drakon.ktdiscord.channel.embed.EmbedImage
import cloud.drakon.ktdiscord.channel.embed.EmbedThumbnail
import kotlinx.coroutines.coroutineScope

suspend fun questHandler(quest: Quest, language: String, lodestone: String) =
    coroutineScope {
        val image = if (quest.banner != "") {
            EmbedImage(url = "https://xivapi.com${quest.banner}")
        } else {
            null
        }

        val embedFields = mutableListOf(
            EmbedField(
                name = Localisation.level.getValue(language),
                value = quest.classJobLevel
            )
        )

        if (quest.experiencePoints > 0) {
            embedFields.add(
                EmbedField(
                    name = Localisation.experience.getValue(language),
                    value = "${
                        String.format(
                            "%,d",
                            quest.experiencePoints
                        )
                    } <:exp:474543347965362176>",
                    inline = true
                )
            )
        }

        if (quest.gilReward > 0) {
            embedFields.add(
                EmbedField(
                    name = Localisation.gil.getValue(language),
                    value = "${
                        String.format(
                            "%,d",
                            quest.gilReward
                        )
                    } <:gil:235457032616935424>",
                    inline = true
                )
            )
        }

        return@coroutineScope Embed(
            title = quest.name,
            description = quest.journalGenre.journalCategory.name,
            url = "https://$lodestone.finalfantasyxiv.com/lodestone/playguide/db/search/?q=${
                quest.name.replace(
                    " ",
                    "+"
                )
            }&category=quest",
            image = image,
            thumbnail = EmbedThumbnail(url = "https://xivapi.com${quest.journalGenre.icon}"),
            fields = embedFields.toTypedArray()
        )
    }
