package cloud.drakon.dynamisbot.interact.commands

import cloud.drakon.dynamisbot.interact.Handler.Companion.ktDiscord
import cloud.drakon.ktdiscord.channel.embed.Embed
import cloud.drakon.ktdiscord.channel.embed.EmbedField
import cloud.drakon.ktdiscord.channel.embed.EmbedThumbnail
import cloud.drakon.ktdiscord.interaction.Interaction
import cloud.drakon.ktdiscord.interaction.applicationcommand.ApplicationCommandData
import cloud.drakon.ktdiscord.webhook.EditWebhookMessage
import cloud.drakon.ktuniversalis.KtUniversalis
import cloud.drakon.ktxivapi.KtXivApi
import cloud.drakon.ktxivapi.search.StringAlgo
import com.amazonaws.services.lambda.runtime.LambdaLogger
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

    val xivApiItem = KtXivApi.search(
        item,
        listOf("Item"),
        StringAlgo.fuzzy,
        columns = listOf("CanBeHq", "Description", "IconHD", "ID", "Name")
    ).jsonObject["Results"]?.jsonArray?.getOrNull(0)

    if (xivApiItem != null) {
        val xivApiItemId = xivApiItem.jsonObject["ID"]!!.jsonPrimitive.int

        val canBeHighQuality: Boolean =
            xivApiItem.jsonObject["CanBeHq"]!!.jsonPrimitive.int == 1
        val description: String = Jsoup.clean(
            xivApiItem.jsonObject["Description"]!!.jsonPrimitive.content,
            "",
            Safelist.none(),
            Document.OutputSettings().prettyPrint(false)
        ).replace("""\n{3,}""".toRegex(), "\n\n")

        val marketBoardCurrentData = if (highQuality == true && canBeHighQuality) {
            KtUniversalis.getMarketBoardCurrentData(
                world,
                arrayOf(xivApiItemId).toIntArray(),
                entries = 5,
                listings = 5,
                hq = true
            )
        } else if (highQuality == false) {
            KtUniversalis.getMarketBoardCurrentData(
                world,
                arrayOf(xivApiItemId).toIntArray(),
                entries = 5,
                listings = 5,
                hq = false
            )
        } else {
            KtUniversalis.getMarketBoardCurrentData(
                world, arrayOf(xivApiItemId).toIntArray(), entries = 5, listings = 5
            )
        }
        val marketBoardListings = marketBoardCurrentData.listings
        var listings = ""
        var totalPrices = ""
        val gil = "<:gil:235457032616935424>"

        if (!marketBoardListings.isNullOrEmpty()) {
            for (i in marketBoardListings) {
                listings += String.format(
                    "%,d", i.pricePerUnit
                ) + " $gil x " + i.quantity + " [" + i.worldName + "]" + if (i.hq) {
                    " <:hq:916051971063054406>\n"
                } else {
                    "\n"
                }
                totalPrices += String.format(
                    "%,d", i.total
                ) + " $gil\n"
            }
        } else {
            listings = "None"
            totalPrices = "N/A"
        }

        ktDiscord.editOriginalInteractionResponse(
            EditWebhookMessage(
                embeds = arrayOf(
                    Embed(
                        title = "Current prices for " + xivApiItem.jsonObject["Name"]!!.jsonPrimitive.content,
                        description = description,
                        url = "https://universalis.app/market/$xivApiItemId",
                        thumbnail = EmbedThumbnail("https://xivapi.com" + xivApiItem.jsonObject["IconHD"]!!.jsonPrimitive.content),
                        fields = arrayOf(
                            if (highQuality == true && canBeHighQuality && (marketBoardCurrentData.currentAveragePriceHq > 0)) {
                                EmbedField(
                                    name = "Current average price (HQ)",
                                    value = String.format(
                                        "%,f",
                                        marketBoardCurrentData.currentAveragePriceHq
                                    ).trimEnd('0') + " $gil",
                                    inline = false
                                )
                            } else if (highQuality == false && (marketBoardCurrentData.currentAveragePriceNq > 0)) {
                                EmbedField(
                                    name = "Current average price (NQ)",
                                    value = String.format(
                                        "%,f",
                                        marketBoardCurrentData.currentAveragePriceNq
                                    ).trimEnd('0') + " $gil",
                                    inline = false
                                )
                            } else if (highQuality == null && (marketBoardCurrentData.currentAveragePrice > 0)) {
                                EmbedField(
                                    name = "Current average price",
                                    value = String.format(
                                        "%,f",
                                        marketBoardCurrentData.currentAveragePrice
                                    ).trimEnd('0') + " $gil",
                                    inline = false
                                )
                            } else {
                                EmbedField(
                                    name = "Current average price",
                                    value = "N/A",
                                    inline = false
                                )
                            },
                            if (highQuality == true && canBeHighQuality && (marketBoardCurrentData.averagePriceHq > 0)) {
                                EmbedField(
                                    name = "Historic average price (HQ)",
                                    value = String.format(
                                        "%,f", marketBoardCurrentData.averagePriceHq
                                    ).trimEnd('0') + " $gil",
                                    inline = false
                                )
                            } else if (highQuality == false && (marketBoardCurrentData.averagePriceNq > 0)) {
                                EmbedField(
                                    name = "Historic average price (NQ)",
                                    value = String.format(
                                        "%,f", marketBoardCurrentData.averagePriceNq
                                    ).trimEnd('0') + " $gil",
                                    inline = false
                                )
                            } else if (highQuality == null && (marketBoardCurrentData.averagePrice > 0)) {
                                EmbedField(
                                    name = "Historic average price",
                                    value = String.format(
                                        "%,f", marketBoardCurrentData.averagePrice
                                    ).trimEnd('0') + " $gil",
                                    inline = false
                                )
                            } else {
                                EmbedField(
                                    name = "Historic average price",
                                    value = "N/A",
                                    inline = false
                                )
                            },
                            EmbedField(
                                name = "Listings", value = listings, inline = true
                            ),
                            EmbedField(
                                name = "Total price", value = totalPrices, inline = true
                            )
                        )
                    )
                )
            ), event.token
        )
    } else {
        ktDiscord.editOriginalInteractionResponse(
            EditWebhookMessage(content = "Could not find item \"$item\""), event.token
        )
    }
}
