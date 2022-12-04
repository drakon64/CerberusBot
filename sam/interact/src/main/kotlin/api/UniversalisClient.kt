package cloud.drakon.tempestbot.interact.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.java.Java
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText

class UniversalisClient(private val ktorClient: HttpClient = HttpClient(Java)) {
    suspend fun getMarketBoardCurrentData(
        itemIds: Short,
        worldDcRegion: String,
        listings: Byte? = null,
        entries: Byte? = null,
        noGst: Boolean? = null,
        hq: Boolean? = null,
        statsWithin: ULong? = null,
        entriesWithin: ULong? = null,
        fields: String? = null,
    ): String {
        return ktorClient.get("https://universalis.app/api/v2/$worldDcRegion/$itemIds") {
            url {
                if (listings != null) {
                    parameters.append("listings", listings.toString())
                }
                if (entries != null) {
                    parameters.append("entries", entries.toString())
                }
                if (noGst != null) {
                    parameters.append("noGst", noGst.toString())
                }
                if (hq != null) {
                    parameters.append("hq", hq.toString())
                }
                if (statsWithin != null) {
                    parameters.append("statsWithin", statsWithin.toString())
                }
                if (entriesWithin != null) {
                    parameters.append("entriesWithin", entriesWithin.toString())
                }
                if (fields != null) {
                    parameters.append("fields", fields)
                }
            }
        }.bodyAsText()
    }
}
