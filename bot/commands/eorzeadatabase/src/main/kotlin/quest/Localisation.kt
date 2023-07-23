package cloud.drakon.dynamisbot.eorzeadatabase.quest

internal object Localisation {
    val level = mapOf("ja" to "Lv", "de" to "St.", "fr" to "Niv.").withDefault { "Lv." }

    val experience =
        mapOf(
            "ja" to "経験値",
            "de" to "Routine",
            "fr" to "Expérience"
        ).withDefault { "Experience" }

    val gil = mapOf("ja" to "ギル", "fr" to "Gils").withDefault { "Gil" }
}
