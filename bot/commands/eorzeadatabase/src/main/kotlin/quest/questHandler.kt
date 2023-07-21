package cloud.drakon.dynamisbot.eorzeadatabase.quest

import cloud.drakon.ktdiscord.channel.embed.Embed
import cloud.drakon.ktdiscord.channel.embed.EmbedImage
import cloud.drakon.ktdiscord.channel.embed.EmbedThumbnail
import kotlinx.coroutines.coroutineScope

suspend fun questHandler(quest: Quest, lodestone: String) =
    coroutineScope {
        return@coroutineScope Embed(
            title = quest.name,
            description = quest.journalGenre.name,
            url = "https://$lodestone.finalfantasyxiv.com/lodestone/playguide/db/search/?q=${
                quest.name.replace(
                    " ",
                    "+"
                )
            }&db_search_category=quest",
            image = EmbedImage(url = "https://xivapi.com${quest.banner}"),
            thumbnail = EmbedThumbnail(url = quest.journalGenre.icon)
        )
    }
