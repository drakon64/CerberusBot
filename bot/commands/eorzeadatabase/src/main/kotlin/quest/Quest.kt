package cloud.drakon.dynamisbot.eorzeadatabase.quest

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable class Quest(
    @SerialName("Name") val name: String,
    @SerialName("JournalGenre") val journalGenre: JournalGenre,
    @SerialName("Banner") val banner: String,
    @SerialName("ClassJobLevel0") val classJobLevel: Byte,
    @SerialName("ExperiencePoints") val experiencePoints: Short,
    @SerialName("GilReward") val gilReward: Short
) {
    @Serializable class JournalGenre(
        @SerialName("IconHD") val icon: String,
        @SerialName("JournalCategory") val journalCategory: JournalCategory
    ) {
        @Serializable class JournalCategory(@SerialName("Name") val name: String)
    }
}
