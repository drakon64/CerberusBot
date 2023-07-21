package cloud.drakon.dynamisbot.eorzeadatabase.quest

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable class JournalGenre(
    @SerialName("IconHD") val icon: String,
    @SerialName("JournalCategory") val journalCategory: JournalCategory
)
