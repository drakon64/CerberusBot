package cloud.drakon.tempestbot.interact.commands.ffxiv

import cloud.drakon.ktdiscord.interaction.Interaction
import cloud.drakon.ktdiscord.interaction.applicationcommand.ApplicationCommandData
import cloud.drakon.ktdiscord.webhook.EditWebhookMessage
import cloud.drakon.ktxivapi.KtXivApi
import cloud.drakon.ktxivapi.search.StringAlgo
import cloud.drakon.tempestbot.interact.Handler.Companion.ktDiscord
import cloud.drakon.tempestbot.interact.commands.ffxiv.eorzeadatabase.medicineMeal
import com.amazonaws.services.lambda.runtime.LambdaLogger
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

suspend fun eorzeaDatabase(
    event: Interaction<ApplicationCommandData>,
    logger: LambdaLogger,
) {
    logger.log("Responding to Eorzea Database command")

    lateinit var index: String
    lateinit var string: String

    for (i in event.data !!.options !!) {
        when (i.name) {
            "index" -> index = i.value !!
            "string" -> string = i.value !!
        }
    }

    val search = KtXivApi.search(
        string, indexes = listOf(index), stringAlgo = StringAlgo.fuzzy, limit = 1
    )
    val id = search.results[0].id
    val item = KtXivApi.getContentId(index, id)
    val itemKind = item["ItemKind"] !!.jsonObject["ID"] !!.jsonPrimitive.int

    val embed = when (itemKind) {
        5 -> { // Medicines & Meals
            medicineMeal(item)
        }

        else -> {
            throw Throwable("Unknown item type: ${item["ItemKind"] !!.jsonObject["Name"] !!.jsonPrimitive.content}")
        }
    }

    ktDiscord.editOriginalInteractionResponse(
        EditWebhookMessage(embeds = arrayOf(embed)), event.token
    )
}
