package cloud.drakon.dynamisbot.eorzeadatabase

import cloud.drakon.dynamisbot.eorzeadatabase.Handler.Companion.json
import cloud.drakon.dynamisbot.eorzeadatabase.Handler.Companion.ktDiscord
import cloud.drakon.dynamisbot.eorzeadatabase.item.itemHandler
import cloud.drakon.dynamisbot.eorzeadatabase.quest.questHandler
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

    lateinit var index: String
    lateinit var query: String
    var language: String? = null

    for (i in event.data!!.options!!) {
        when (i.name) {
            "index" -> index = i.value!!
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

    val search = KtXivApi.search(
        query,
        indexes = listOf(index),
        stringAlgo = StringAlgo.fuzzy,
        limit = 1,
        language = searchLanguage
    ).jsonObject["Results"]!!.jsonArray.getOrNull(0)

    if (search != null) {
        val id = search.jsonObject["ID"]!!.jsonPrimitive.int
        val result = KtXivApi.getContentId(index, id, searchLanguage)

        val embed = when (index) {
            "item" -> itemHandler(result, language, lodestone)
            "quest" -> questHandler(json.decodeFromJsonElement(result), lodestone)
            else -> throw Throwable("Unknown index: \"$index\"")
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
