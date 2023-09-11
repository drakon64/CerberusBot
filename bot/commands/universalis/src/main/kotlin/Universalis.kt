package cloud.drakon.dynamisbot.universalis

import cloud.drakon.dynamisbot.universalis.Handler.Companion.json
import cloud.drakon.dynamisbot.universalis.Handler.Companion.ktDiscord
import cloud.drakon.dynamisbot.universalis.Handler.Companion.ktXivApi
import cloud.drakon.dynamisbot.universalis.Handler.Companion.newLineRegex
import cloud.drakon.dynamisbot.universalis.Handler.Companion.spanRegex
import cloud.drakon.ktdiscord.channel.embed.Embed
import cloud.drakon.ktdiscord.channel.embed.EmbedField
import cloud.drakon.ktdiscord.channel.embed.EmbedThumbnail
import cloud.drakon.ktdiscord.interaction.Interaction
import cloud.drakon.ktdiscord.interaction.interactiondata.ApplicationCommandData
import cloud.drakon.ktdiscord.webhook.EditWebhookMessage
import cloud.drakon.ktuniversalis.getMarketBoardCurrentData
import cloud.drakon.ktuniversalis.world.World
import cloud.drakon.ktxivapi.search.StringAlgo
import com.amazonaws.services.lambda.runtime.LambdaLogger
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject

const val gil = "<:gil:235457032616935424>"

suspend fun universalisCommand(
    event: Interaction<ApplicationCommandData>,
    logger: LambdaLogger,
) {
    logger.log("Responding to Universalis command")

    lateinit var item: String
    lateinit var world: World
    var highQuality: Boolean? = null

    when (event.data!!.type) {
        1 -> for (i in event.data!!.options!!) {
            when (i.name) {
                "item" -> item = i.value!!
                "world" -> world = World.valueOf(i.value!!)
                "high_quality" -> highQuality = i.value!!.toBooleanStrict()
            }
        }

        else -> logger.log("Unknown application command type: " + event.data!!.type)
    }

    val result = ktXivApi.search(
        item,
        listOf("Item"),
        StringAlgo.fuzzy,
        columns = listOf("Name", "Description", "ID", "IconHD", "CanBeHq")
    ).jsonObject["Results"]?.jsonArray?.getOrNull(0)

    if (result != null) {
        val xivApiItem = json.decodeFromJsonElement<Item>(result)
        val canBeHq = xivApiItem.canBeHq == 1

        val description = xivApiItem.description
            .replace(spanRegex, "")
            .replace(newLineRegex, "\n\n")

        val marketBoardCurrentData = if (highQuality == true && canBeHq) {
            getMarketBoardCurrentData(
                world, xivApiItem.id, 5, 0, hq = true
            )
        } else if (highQuality == false) {
            getMarketBoardCurrentData(
                world, xivApiItem.id, 5, 0, hq = false
            )
        } else {
            getMarketBoardCurrentData(
                world, xivApiItem.id, 5, 0
            )
        }

        val marketBoardListings = marketBoardCurrentData.listings
        val listings = mutableListOf<String>()

        if (marketBoardListings.isNullOrEmpty()) {
            listings.add("None")
        } else {
            for (listing in marketBoardListings) {
                val pricePerUnit = String.format("%,d", listing.pricePerUnit)
                val totalPrice = String.format(
                    "%,d", listing.pricePerUnit * listing.quantity
                )

                var listingString =
                    "$pricePerUnit $gil x ${listing.quantity} ($totalPrice) [${listing.worldName}]"
                if (highQuality == true) {
                    listingString += " <:hq:916051971063054406>"
                }

                listings.add(listingString)
            }
        }

        val currentAveragePriceField = when {
            highQuality == true && canBeHq -> "Current average price (HQ)"
            highQuality == false -> "Current average price (NQ)"
            else -> "Current average price"
        }

        val currentAveragePrice = when {
            highQuality == true && canBeHq -> marketBoardCurrentData.currentAveragePriceHq
            highQuality == false -> marketBoardCurrentData.currentAveragePriceNq
            else -> marketBoardCurrentData.currentAveragePrice
        }.let { String.format("%,f", it).trimEnd('0') + " $gil" }

        ktDiscord.editOriginalInteractionResponse(
            EditWebhookMessage(
                embeds = listOf(
                    Embed(
                        title = "Current prices for ${xivApiItem.name}",
                        description = description,
                        url = "https://universalis.app/market/$xivApiItem.id",
                        thumbnail = EmbedThumbnail("https://xivapi.com${xivApiItem.iconHd}"),
                        fields = listOf(
                            EmbedField(currentAveragePriceField, currentAveragePrice),
                            EmbedField("Listings", listings.joinToString("\n"))
                        )
                    )
                )
            ), event.token
        )
    } else {
        ktDiscord.editOriginalInteractionResponse(
            EditWebhookMessage("Could not find item \"$item\""), event.token
        )
    }
}
