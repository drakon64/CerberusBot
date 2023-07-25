package cloud.drakon.dynamisbot.universalis

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable class Item(
    @SerialName("Name") val name: String,
    @SerialName("Description") val description: String,
    @SerialName("ID") val id: Int,
    @SerialName("IconHD") val iconHd: String,
    @SerialName("CanBeHq") val canBeHq: Boolean
)
