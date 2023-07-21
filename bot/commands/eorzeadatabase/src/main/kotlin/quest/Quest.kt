package cloud.drakon.dynamisbot.eorzeadatabase.quest

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable class Quest(
    @SerialName("Name") val name: String,
    @SerialName("JournalGenre") val journalGenre: JournalGenre,
    @SerialName("Banner") val banner: String,
    @SerialName("ClassJobLevel0") val classJobLevel: Byte,
    @SerialName("GilReward") val gilReward: Short
)
