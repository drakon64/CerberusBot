package cloud.drakon.tempestbot.interact.commands.ffxiv.eorzeadatabase

import cloud.drakon.ktdiscord.interaction.Interaction
import cloud.drakon.ktdiscord.interaction.applicationcommand.ApplicationCommandData
import cloud.drakon.ktdiscord.webhook.EditWebhookMessage
import cloud.drakon.ktxivapi.KtXivApi
import cloud.drakon.ktxivapi.common.Language
import cloud.drakon.ktxivapi.search.StringAlgo
import cloud.drakon.tempestbot.interact.Handler.Companion.ktDiscord
import com.amazonaws.services.lambda.runtime.LambdaLogger
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.safety.Safelist

suspend fun eorzeaDatabase(
    event: Interaction<ApplicationCommandData>,
    logger: LambdaLogger,
) {
    logger.log("Responding to Eorzea Database command")

    lateinit var index: String
    lateinit var string: String
    var language: String? = null

    for (i in event.data !!.options !!) {
        when (i.name) {
            "index" -> index = i.value !!
            "string" -> string = i.value !!
            "language" -> language = i.value !!
        }
    }

    val searchLanguage = if (language != null) {
        when (language) {
            "en" -> Language.en
            "ja" -> Language.ja
            "de" -> Language.de
            "fr" -> Language.fr
            else -> throw Throwable("Unknown language: \"$language\"")
        }
    } else {
        null
    }

    val search = KtXivApi.search(
        string,
        indexes = listOf(index),
        stringAlgo = StringAlgo.fuzzy,
        limit = 1,
        language = searchLanguage
    ).jsonObject["Results"] !!.jsonArray.getOrNull(0)

    if (search != null) {
        val id = search.jsonObject["ID"] !!.jsonPrimitive.int
        val item = KtXivApi.getContentId(index, id, searchLanguage)

        val embed = when (item["ItemKind"] !!.jsonObject["ID"] !!.jsonPrimitive.int) {
            1 -> { // Arms
                arms(item, language)
            }

            5 -> { // Medicines & Meals
                medicineMeal(
                    item, cleanDescription(item["Description"] !!.jsonPrimitive.content)
                )
            }

            else -> {
                throw Throwable("Unknown item type: ${item["ItemKind"] !!.jsonObject["Name"] !!.jsonPrimitive.content}")
            }
        }

        ktDiscord.editOriginalInteractionResponse(
            EditWebhookMessage(embeds = arrayOf(embed)), event.token
        )
    } else {
        ktDiscord.editOriginalInteractionResponse(
            EditWebhookMessage(content = "Could not find $index \"$string\""),
            event.token
        )
    }
}
