package cloud.drakon.dynamisbot.eorzeadatabase.quest

import cloud.drakon.ktdiscord.channel.embed.Embed
import cloud.drakon.ktdiscord.channel.embed.EmbedField
import cloud.drakon.ktdiscord.channel.embed.EmbedImage
import cloud.drakon.ktdiscord.channel.embed.EmbedThumbnail
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable class Quest(
    @SerialName("Name") val name: String,
    @SerialName("JournalGenre") val journalGenre: JournalGenre,
    @SerialName("Banner") val banner: String,
    @SerialName("ClassJobLevel0") val classJobLevel: String,
    @SerialName("ExperiencePoints") val experiencePoints: Short,
    @SerialName("GilReward") val gilReward: Short
) {
    @Serializable class JournalGenre(
        @SerialName("Name") val name: String,
        @SerialName("IconHD") val icon: String,
        @SerialName("JournalCategory") val journalCategory: JournalCategory
    ) {
        @Serializable class JournalCategory(@SerialName("Name") val name: String)
    }

//    private val genre = mapOf("" to "").withDefault { "Genre" }

//    private val category = mapOf("" to "").withDefault { "Category" }

    private val level = mapOf(
        "ja" to "Lv", "de" to "St.", "fr" to "Niv."
    ).withDefault { "Lv." }

    private val experience = mapOf(
        "ja" to "経験値",
        "de" to "Routine",
        "fr" to "Expérience"
    ).withDefault { "Experience" }

    private val gil = mapOf("ja" to "ギル", "fr" to "Gils").withDefault { "Gil" }

    suspend fun createEmbed(
        language: String,
        lodestone: String
    ) = coroutineScope {
        val image = if (this@Quest.banner != "") {
            EmbedImage(url = "https://xivapi.com${this@Quest.banner}")
        } else {
            null
        }

        val embedFields = mutableListOf(
            EmbedField(
                name = "Category",
                value = this@Quest.journalGenre.journalCategory.name,
                inline = true
            ),
            EmbedField(
                name = "Subcategory",
                value = this@Quest.journalGenre.name,
                inline = true
            ),
            EmbedField(
                name = level.getValue(language),
                value = this@Quest.classJobLevel
            )
        )

        if (this@Quest.experiencePoints > 0) {
            embedFields.add(
                EmbedField(
                    name = experience.getValue(language),
                    value = "${
                        String.format(
                            "%,d",
                            this@Quest.experiencePoints
                        )
                    } <:exp:474543347965362176>",
                    inline = true
                )
            )
        }

        if (this@Quest.gilReward > 0) {
            embedFields.add(
                EmbedField(
                    name = gil.getValue(language),
                    value = "${
                        String.format(
                            "%,d",
                            this@Quest.gilReward
                        )
                    } <:gil:235457032616935424>",
                    inline = true
                )
            )
        }

        return@coroutineScope Embed(
            title = this@Quest.name,
            url = "https://$lodestone.finalfantasyxiv.com/lodestone/playguide/db/search/?q=${
                this@Quest.name.replace(
                    " ",
                    "+"
                )
            }&category=quest",
            image = image,
            thumbnail = EmbedThumbnail(url = "https://xivapi.com${this@Quest.journalGenre.icon}"),
            fields = embedFields.toTypedArray()
        )
    }
}
