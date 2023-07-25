package cloud.drakon.dynamisbot.eorzeadatabase

import cloud.drakon.dynamisbot.eorzeadatabase.Handler.Companion.json
import cloud.drakon.dynamisbot.eorzeadatabase.Handler.Companion.ktDiscord
import cloud.drakon.dynamisbot.eorzeadatabase.item.itemHandler
import cloud.drakon.dynamisbot.eorzeadatabase.quest.Quest
import cloud.drakon.ktdiscord.interaction.Interaction
import cloud.drakon.ktdiscord.interaction.applicationcommand.ApplicationCommandData
import cloud.drakon.ktdiscord.webhook.EditWebhookMessage
import cloud.drakon.ktxivapi.KtXivApi
import cloud.drakon.ktxivapi.common.Language
import cloud.drakon.ktxivapi.search.StringAlgo
import com.amazonaws.services.lambda.runtime.LambdaLogger
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

suspend fun eorzeaDatabase(
    event: Interaction<ApplicationCommandData>,
    logger: LambdaLogger,
) {
    logger.log("Responding to Eorzea Database command")

    lateinit var index: Index
    lateinit var query: String
    var language: String? = null

    for (i in event.data!!.options!!) {
        when (i.name) {
            "index" -> index = Index.valueOf(i.value!!)
            "query" -> query = i.value!!
            "language" -> language = i.value!!
        }
    }

    if (language == null) {
        language = when (event.locale) {
            "ja", "de", "fr" -> event.locale
            else -> "en"
        }
    }

    val searchLanguage: Language
    val lodestone: String
    when (language) {
        "en" -> {
            searchLanguage = Language.en
            lodestone = "eu"
        }

        "ja" -> {
            searchLanguage = Language.ja
            lodestone = "jp"
        }

        "de" -> {
            searchLanguage = Language.de
            lodestone = "de"
        }

        "fr" -> {
            searchLanguage = Language.fr
            lodestone = "fr"
        }

        else -> throw Throwable("Unknown language: \"$language\"")
    }

    val columns = if (index.name == "quest") {
        listOf(
            "Name",
            "Expansion.Name",
            "JournalGenre.Name",
            "JournalGenre.IconHD",
            "JournalGenre.JournalCategory.Name",
            "Banner",
            "ClassJobLevel0",
            "ExperiencePoints",
            "GilReward"
        )
    } else {
        null
    }

    val search = KtXivApi.search(
        query,
        indexes = listOf(index.name),
        stringAlgo = StringAlgo.fuzzy,
        limit = 1,
        language = searchLanguage,
        columns = columns
    ).jsonObject["Results"]!!.jsonArray.getOrNull(0)

    if (search != null) {
        val result = if (index != Index.quest) {
            KtXivApi.getContentId(
                index.name,
                search.jsonObject["ID"]!!.jsonPrimitive.int,
                searchLanguage
            )
        } else {
            search
        }

        val embed = when (index) {
            Index.item -> itemHandler(result, language, lodestone)

            Index.quest -> json.decodeFromJsonElement<Quest>(search)
                .createEmbed(language, lodestone)
        }

        ktDiscord.editOriginalInteractionResponse(
            EditWebhookMessage(
                embeds = arrayOf(embed)
            ), event.token
        )
    } else {
        ktDiscord.editOriginalInteractionResponse(
            EditWebhookMessage(content = "Could not find $index \"$query\""),
            event.token
        )
    }
}
