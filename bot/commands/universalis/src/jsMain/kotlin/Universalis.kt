package cloud.drakon.dynamisbot.universalis

import cloud.drakon.ktdiscord.channel.embed.Embed
import cloud.drakon.ktdiscord.channel.embed.EmbedField
import cloud.drakon.ktdiscord.channel.embed.EmbedThumbnail
import cloud.drakon.ktdiscord.interaction.Interaction
import cloud.drakon.ktdiscord.interaction.applicationcommand.ApplicationCommandData
import cloud.drakon.ktdiscord.webhook.EditWebhookMessage
import cloud.drakon.ktxivapi.search.StringAlgo
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject

suspend fun universalisCommand(event: Interaction<ApplicationCommandData>) =
    coroutineScope {
        lateinit var item: String
        lateinit var world: String
        var highQuality: Boolean? = null

        for (i in event.data!!.options!!) {
            when (i.name) {
                "item" -> item = i.value!!
                "world" -> world = i.value!!
                "high_quality" -> highQuality = i.value!!.toBooleanStrict()
            }
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
                ktUniversalis.getMarketBoardCurrentData(
                    world,
                    listOf(xivApiItem.id),
                    entries = 5,
                    listings = 5,
                    hq = true
                )
            } else if (highQuality == false) {
                ktUniversalis.getMarketBoardCurrentData(
                    world,
                    listOf(xivApiItem.id),
                    entries = 5,
                    listings = 5,
                    hq = false
                )
            } else {
                ktUniversalis.getMarketBoardCurrentData(
                    world,
                    listOf(xivApiItem.id),
                    entries = 5,
                    listings = 5
                )
            }

            val marketBoardListings =
                marketBoardCurrentData.listings // TODO: Confirm if this is needed with K2 compiler
            val listings = mutableListOf<String>()
            val gil = "<:gil:235457032616935424>"

            if (marketBoardListings.isNullOrEmpty()) {
                listings.add("None")
            } else {
                for (listing in marketBoardListings) {
                    // TODO: Replace with Number.toLocaleString('en')
                    val pricePerUnit = js("listing.pricePerUnit.toLocaleString(\"en\")")
                    val totalPrice =
                        js("(listing.pricePerUnit * listing.quantity).toLocaleString(\"en\")")

                    var listingString =
                        "$pricePerUnit $gil x ${listing.quantity} ($totalPrice) [${listing.worldName}]"
                    if (highQuality == true) {
                        listingString += " <:hq:916051971063054406>"
                    }

                    listings.add(listingString)
                }
            }

            val currentAveragePriceField = if (highQuality == true && canBeHq) {
                "Current average price (HQ)"
            } else if (highQuality == false) {
                "Current average price (NQ)"
            } else {
                "Current average price"
            }

            val historicAveragePriceField = if (highQuality == true && canBeHq) {
                "Historic average price (HQ)"
            } else if (highQuality == false) {
                "Historic average price (NQ)"
            } else {
                "Historic average price"
            }

            val currentAveragePrice = if (highQuality == true && canBeHq) {
                // TODO: Replace with Number.toLocaleString('en')
                val currentAveragePrice = marketBoardCurrentData.currentAveragePriceHq
                    .toString()
                    .trimEnd('0') + " $gil"
                js("currentAveragePrice.toLocaleString(\"en\")")
            } else if (highQuality == false) {
                // TODO: Replace with Number.toLocaleString('en')
                val currentAveragePrice = marketBoardCurrentData.currentAveragePriceNq
                    .toString()
                    .trimEnd('0') + " $gil"
                js("currentAveragePrice.toLocaleString(\"en\")")
            } else {
                // TODO: Replace with Number.toLocaleString('en')
                val currentAveragePrice = marketBoardCurrentData.currentAveragePrice
                    .toString()
                    .trimEnd('0') + " $gil"
                js("currentAveragePrice.toLocaleString(\"en\")")
            }

            val currentAveragePriceEmbed = EmbedField(
                currentAveragePriceField, currentAveragePrice, true
            )

            val historicAveragePrice = if (highQuality == true && canBeHq) {
                // TODO: Replace with Number.toLocaleString('en')
                val historicAveragePrice = marketBoardCurrentData.averagePriceHq
                    .toString()
                    .trimEnd('0') + " $gil"
                js("historicAveragePrice.toLocaleString(\"en\")")
            } else if (highQuality == false) {
                // TODO: Replace with Number.toLocaleString('en')
                val historicAveragePrice = marketBoardCurrentData.averagePriceNq
                    .toString()
                    .trimEnd('0') + " $gil"
                js("historicAveragePrice.toLocaleString(\"en\")")
            } else {
                // TODO: Replace with Number.toLocaleString('en')
                val historicAveragePrice = marketBoardCurrentData.averagePrice
                    .toString()
                    .trimEnd('0') + " $gil"
                js("historicAveragePrice.toLocaleString(\"en\")")
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
