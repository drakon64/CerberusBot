package cloud.drakon.tempestbot.interact.commands.ffxiv

import cloud.drakon.tempest.channel.embed.Embed
import cloud.drakon.tempest.channel.embed.EmbedField
import cloud.drakon.tempest.channel.embed.EmbedThumbnail
import cloud.drakon.tempest.interaction.Interaction
import cloud.drakon.tempest.interaction.applicationcommand.ApplicationCommandData
import cloud.drakon.tempest.webbook.EditWebhookMessage
import cloud.drakon.tempestbot.interact.Handler
import cloud.drakon.tempestbot.interact.api.XivApiClient
import com.amazonaws.services.lambda.runtime.LambdaLogger
import io.ktor.client.HttpClient
import io.ktor.client.engine.java.Java
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.safety.Safelist

suspend fun universalis(
    event: Interaction<ApplicationCommandData>,
    logger: LambdaLogger,
) {
    logger.log("Responding to Universalis command")

    lateinit var item: String
    lateinit var world: String
    var highQuality: Boolean? = null

    when (event.data !!.type) {
        1 -> for (i in event.data !!.options !!) {
            when (i.name) {
                "item" -> item = i.value !!
                "world" -> world = i.value !!
                "high_quality" -> highQuality = i.value.toBoolean()
            }
        }

        else -> logger.log("Unknown application command type: " + event.data !!.type)
    }

    val ktorClient = HttpClient(Java)
    val xivApi = XivApiClient(ktorClient = ktorClient)

    val xivApiItemId = Json.parseToJsonElement(
        xivApi.search(
            item, "Item"
        )
    ).jsonObject["Results"] !!.jsonArray[0].jsonObject["ID"] !!.jsonPrimitive.int
    val xivApiItem = Json.parseToJsonElement(xivApi.item(xivApiItemId))

    val canBeHighQuality: Boolean =
        xivApiItem.jsonObject["CanBeHq"] !!.jsonPrimitive.int == 1
    val description: String =
        Jsoup.clean(
            xivApiItem.jsonObject["Description"] !!.jsonPrimitive.content,
            "",
            Safelist.none(),
            Document.OutputSettings().prettyPrint(false)
        )

    val embedField: EmbedField = if (canBeHighQuality) {
        when (highQuality) {
            true -> EmbedField(name = "Current average price (HQ)", value = "listings")
            false -> EmbedField(
                name = "Current average price (NQ)", value = ""
            )

            null -> EmbedField(name = "Current average price", value = "listings")
        }
    } else {
        EmbedField(name = "Current average price (NQ)", value = "listings")
    }

    Handler.tempestClient.editOriginalInteractionResponse(
        EditWebhookMessage(
            embeds = arrayOf(
                Embed(
                    title = "Current prices for " + xivApiItem.jsonObject["Name"] !!.jsonPrimitive.content,
                    description = description,
                    url = "https://universalis.app/market/$xivApiItemId",
                    thumbnail = EmbedThumbnail("https://xivapi.com" + xivApiItem.jsonObject["IconHD"] !!.jsonPrimitive.content),
                    fields = arrayOf(embedField)
                )
            )
        ), event.token
    )
}
