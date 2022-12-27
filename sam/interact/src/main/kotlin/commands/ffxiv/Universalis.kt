package cloud.drakon.tempestbot.interact.commands.ffxiv

import aws.smithy.kotlin.runtime.util.length
import cloud.drakon.tempest.channel.embed.Embed
import cloud.drakon.tempest.channel.embed.EmbedField
import cloud.drakon.tempest.channel.embed.EmbedThumbnail
import cloud.drakon.tempest.interaction.Interaction
import cloud.drakon.tempest.interaction.applicationcommand.ApplicationCommandData
import cloud.drakon.tempest.webbook.EditWebhookMessage
import cloud.drakon.tempestbot.interact.Handler
import cloud.drakon.tempestbot.interact.api.UniversalisClient
import cloud.drakon.tempestbot.interact.api.XivApiClient
import com.amazonaws.services.lambda.runtime.LambdaLogger
import io.ktor.client.HttpClient
import io.ktor.client.engine.java.Java
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.double
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
                "high_quality" -> highQuality =
                    i.value !!.toBooleanStrict() // TODO https://github.com/TempestProject/Tempest/issues/3
            }
        }

        else -> logger.log("Unknown application command type: " + event.data !!.type)
    }

    val ktorClient = HttpClient(Java)
    val xivApi = XivApiClient(ktorClient = ktorClient)
    val universalisClient = UniversalisClient(ktorClient = ktorClient)

    val xivApiItemId = xivApi.search(
        item, "Item"
    ).jsonObject["Results"] !!.jsonArray[0].jsonObject["ID"] !!.jsonPrimitive.int
    val xivApiItem = xivApi.item(xivApiItemId)

    val canBeHighQuality: Boolean =
        xivApiItem.jsonObject["CanBeHq"] !!.jsonPrimitive.int == 1
    val description: String = Jsoup.clean(
        xivApiItem.jsonObject["Description"] !!.jsonPrimitive.content,
        "",
        Safelist.none(),
        Document.OutputSettings().prettyPrint(false)
    )

    val marketBoardCurrentData = if (highQuality == true && canBeHighQuality) {
        universalisClient.getMarketBoardCurrentData(
            xivApiItemId, world, entries = 5, listings = 5, hq = true
        )
    } else if (highQuality == false) {
        universalisClient.getMarketBoardCurrentData(
            xivApiItemId, world, entries = 5, listings = 5, hq = false
        )
    } else {
        universalisClient.getMarketBoardCurrentData(
            xivApiItemId, world, entries = 5, listings = 5
        )
    }
    val marketBoardListings = marketBoardCurrentData.jsonObject["listings"] !!.jsonArray
    var listings = ""
    var totalPrices = ""
    val gil = "<:gil:235457032616935424>"

    if (marketBoardListings.length > 0) {
        for (i in marketBoardListings) {
            listings += String.format(
                "%,d", i.jsonObject["pricePerUnit"] !!.jsonPrimitive.int
            ) + " $gil x " + i.jsonObject["quantity"] !!.jsonPrimitive.int.toString() + " [" + i.jsonObject["worldName"] !!.jsonPrimitive.content + "]" + if (i.jsonObject["hq"] !!.jsonPrimitive.boolean) {
                " <:hq:916051971063054406>\n"
            } else {
                "\n"
            }
            totalPrices += String.format(
                "%,d", i.jsonObject["total"] !!.jsonPrimitive.int
            ) + " $gil\n"
        }
    } else {
        listings = "None"
        totalPrices = "N/A"
    }

    Handler.tempestClient.editOriginalInteractionResponse(
        EditWebhookMessage(
            embeds = arrayOf(
                Embed(
                    title = "Current prices for " + xivApiItem.jsonObject["Name"] !!.jsonPrimitive.content,
                    description = description,
                    url = "https://universalis.app/market/$xivApiItemId",
                    thumbnail = EmbedThumbnail("https://xivapi.com" + xivApiItem.jsonObject["IconHD"] !!.jsonPrimitive.content),
                    fields = arrayOf(
                        if (highQuality == true && canBeHighQuality && (marketBoardCurrentData.jsonObject["currentAveragePriceHQ"] !!.jsonPrimitive.double > 0)) {
                            EmbedField(
                                name = "Current average price (HQ)",
                                value = String.format(
                                    "%,f",
                                    marketBoardCurrentData.jsonObject["currentAveragePriceHQ"] !!.jsonPrimitive.double
                                ).trimEnd('0') + " $gil",
                                inline = false
                            )
                        } else if (highQuality == false && (marketBoardCurrentData.jsonObject["currentAveragePriceNQ"] !!.jsonPrimitive.double > 0)) {
                            EmbedField(
                                name = "Current average price (NQ)",
                                value = String.format(
                                    "%,f",
                                    marketBoardCurrentData.jsonObject["currentAveragePriceNQ"] !!.jsonPrimitive.double
                                ).trimEnd('0') + " $gil",
                                inline = false
                            )
                        } else if (highQuality == null && (marketBoardCurrentData.jsonObject["currentAveragePrice"] !!.jsonPrimitive.double > 0)) {
                            EmbedField(
                                name = "Current average price", value = String.format(
                                    "%,f",
                                    marketBoardCurrentData.jsonObject["currentAveragePrice"] !!.jsonPrimitive.double
                                ).trimEnd('0') + " $gil", inline = false
                            )
                        } else {
                            EmbedField(
                                name = "Current average price",
                                value = "N/A",
                                inline = false
                            )
                        },
                        if (highQuality == true && canBeHighQuality && (marketBoardCurrentData.jsonObject["averagePriceHQ"] !!.jsonPrimitive.double > 0)) {
                            EmbedField(
                                name = "Historic average price (HQ)",
                                value = String.format(
                                    "%,f",
                                    marketBoardCurrentData.jsonObject["averagePriceHQ"] !!.jsonPrimitive.double
                                ).trimEnd('0') + " $gil",
                                inline = false
                            )
                        } else if (highQuality == false && (marketBoardCurrentData.jsonObject["averagePriceNQ"] !!.jsonPrimitive.double > 0)) {
                            EmbedField(
                                name = "Historic average price (NQ)",
                                value = String.format(
                                    "%,f",
                                    marketBoardCurrentData.jsonObject["averagePriceNQ"] !!.jsonPrimitive.double
                                ).trimEnd('0') + " $gil",
                                inline = false
                            )
                        } else if (highQuality == null && (marketBoardCurrentData.jsonObject["averagePrice"] !!.jsonPrimitive.double > 0)) {
                            EmbedField(
                                name = "Historic average price", value = String.format(
                                    "%,f",
                                    marketBoardCurrentData.jsonObject["averagePrice"] !!.jsonPrimitive.double
                                ).trimEnd('0') + " $gil", inline = false
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
}
