package cloud.drakon.dynamisbot.eorzeadatabase.item

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable sealed interface ArmorAccessoriesShield: StatsItem {
    @SerialName("Name") override val name: String
    @SerialName("IconHD") override val iconHd: String
    @SerialName("Stats") override val stats: Map<String, Map<String, Int>>?

    @SerialName("ClassJobCategory") val classJobCategory: ClassJobCategory
    @SerialName("LevelEquip") val levelEquip: String

    @Serializable class ClassJobCategory(@SerialName("Name") val name: String)
}
