package cloud.drakon.dynamisbot.universalis

import cloud.drakon.dynamisbot.universalis.Handler.Companion.json
import cloud.drakon.dynamisbot.universalis.Handler.Companion.ktDiscord
import cloud.drakon.dynamisbot.universalis.Handler.Companion.ktUniversalis
import cloud.drakon.dynamisbot.universalis.Handler.Companion.ktXivApi
import cloud.drakon.dynamisbot.universalis.Handler.Companion.newLineRegex
import cloud.drakon.dynamisbot.universalis.Handler.Companion.spanRegex
import cloud.drakon.ktdiscord.channel.embed.Embed
import cloud.drakon.ktdiscord.channel.embed.EmbedField
import cloud.drakon.ktdiscord.channel.embed.EmbedThumbnail
import cloud.drakon.ktdiscord.channel.message.Message
import cloud.drakon.ktdiscord.interaction.Interaction
import cloud.drakon.ktdiscord.interaction.applicationcommand.ApplicationCommandData
import cloud.drakon.ktdiscord.webhook.EditWebhookMessage
import cloud.drakon.ktxivapi.search.StringAlgo
import com.amazonaws.services.lambda.runtime.LambdaLogger
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject

suspend fun universalisCommand(
    event: Interaction<ApplicationCommandData>,
    logger: LambdaLogger,
): Message = coroutineScope {
    logger.log("Responding to Universalis command")

    lateinit var item: String
    lateinit var world: String
    var highQuality: Boolean? = null

    when (event.data!!.type) {
        1 -> for (i in event.data!!.options!!) {
            when (i.name) {
                "item" -> item = i.value!!
                "world" -> world = i.value!!
                "high_quality" -> highQuality = i.value!!.toBooleanStrict()
            }
        }

        else -> logger.log("Unknown application command type: " + event.data!!.type)
    }

    val result = ktXivApi.search(
        item,
        listOf("Item"),
        StringAlgo.fuzzy,
        columns = listOf("ID", "CanBeHq", "Description", "ID", "Name")
    ).jsonObject["Results"]?.jsonArray?.getOrNull(0)

    if (result != null) {
        val xivApiItem: Item = json.decodeFromJsonElement(result)

        val description = xivApiItem.description
            .replace(spanRegex, "")
            .replace(newLineRegex, "\n\n")

        val marketBoardCurrentData = if (highQuality == true && xivApiItem.canBeHq) {
            ktUniversalis.getMarketBoardCurrentData(
                world,
                arrayOf(xivApiItem.id).toIntArray(),
                entries = 5,
                listings = 5,
                hq = true
            )
        } else if (highQuality == false) {
            ktUniversalis.getMarketBoardCurrentData(
                world,
                arrayOf(xivApiItem.id).toIntArray(),
                entries = 5,
                listings = 5,
                hq = false
            )
        } else {
            ktUniversalis.getMarketBoardCurrentData(
                world, arrayOf(xivApiItem.id).toIntArray(), entries = 5, listings = 5
            )
        }

        val marketBoardListings = marketBoardCurrentData.listings
        val listings = mutableListOf<String>()
        val gil = "<:gil:235457032616935424>"

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

        val currentAveragePriceField = if (highQuality == true && xivApiItem.canBeHq) {
            "Current average price (HQ)"
        } else if (highQuality == false) {
            "Current average price (NQ)"
        } else {
            "Current average price"
        }

        val historicAveragePriceField = if (highQuality == true && xivApiItem.canBeHq) {
            "Historic average price (HQ)"
        } else if (highQuality == false) {
            "Historic average price (NQ)"
        } else {
            "Historic average price"
        }

        val currentAveragePrice = if (highQuality == true && xivApiItem.canBeHq) {
            String.format("%,f", marketBoardCurrentData.currentAveragePriceHq)
                .trimEnd('0') + " $gil"
        } else if (highQuality == false) {
            String.format("%,f", marketBoardCurrentData.currentAveragePriceNq)
                .trimEnd('0') + " $gil"
        } else {
            String.format("%,f", marketBoardCurrentData.currentAveragePrice)
                .trimEnd('0') + " $gil"
        }

        val currentAveragePriceEmbed = EmbedField(
            currentAveragePriceField, currentAveragePrice, true
        )

        val historicAveragePrice = if (highQuality == true && xivApiItem.canBeHq) {
            String.format("%,f", marketBoardCurrentData.averagePriceHq)
                .trimEnd('0') + " $gil"
        } else if (highQuality == false) {
            String.format("%,f", marketBoardCurrentData.averagePriceNq)
                .trimEnd('0') + " $gil"
        } else {
            String.format("%,f", marketBoardCurrentData.averagePrice)
                .trimEnd('0') + " $gil"
        }

        val historicAveragePriceEmbed = EmbedField(
            historicAveragePriceField, historicAveragePrice, true
        )

        return@coroutineScope ktDiscord.editOriginalInteractionResponse(
            EditWebhookMessage(
                embeds = arrayOf(
                    Embed(
                        title = "Current prices for ${xivApiItem.name}",
                        description = description,
                        url = "https://universalis.app/market/$xivApiItem.id",
                        thumbnail = EmbedThumbnail("https://xivapi.com${xivApiItem.iconHd}"),
                        fields = arrayOf(
                            currentAveragePriceEmbed,
                            historicAveragePriceEmbed,
                            EmbedField(
                                name = "Listings",
                                value = listings.joinToString("\n")
                            )
                        )
                    )
                )
            ), event.token
        )
    } else {
        return@coroutineScope ktDiscord.editOriginalInteractionResponse(
            EditWebhookMessage(content = "Could not find item \"$item\""),
            event.token
        )
    }
}
