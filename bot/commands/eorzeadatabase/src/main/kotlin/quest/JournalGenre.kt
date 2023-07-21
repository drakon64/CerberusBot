package cloud.drakon.dynamisbot.eorzeadatabase.quest

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable class JournalGenre(
    @SerialName("Name") val name: String,
    @SerialName("IconHD") val icon: String
)
