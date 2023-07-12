package cloud.drakon.tempestbot.interact.commands.ffxiv.eorzeadatabase.item

internal object Localisation {
    val autoAttack = mapOf(
        "en" to "Auto-attack",
        "ja" to "物理オートアタック",
        "de" to "Auto-Attacke",
        "fr" to "Attaque auto."
    )

    val bonuses = mapOf(
        "Bonuses" to mapOf(
            "en" to "Bonuses", "ja" to "Bonuses", "de" to "Bonus", "fr" to "Bonus"
        ), "CriticalHit" to mapOf(
            "en" to "Critical Hit",
            "ja" to "クリティカル",
            "de" to "Kritischer Treffer",
            "fr" to "Critique"
        ), "Determination" to mapOf(
            "en" to "Determination",
            "ja" to "意思力",
            "de" to "Entschlossenheit",
            "fr" to "Détermination"
        ), "DirectHitRate" to mapOf(
            "en" to "Direct Hit Rate",
            "ja" to "ダイレクトヒット",
            "de" to "Direkter Treffer",
            "fr" to "Coups nets"
        ), "Intelligence" to mapOf(
            "en" to "Intelligence",
            "ja" to "INT",
            "de" to "Intelligenz",
            "fr" to "Intelligence"
        ), "SkillSpeed" to mapOf(
            "en" to "Skill Speed",
            "ja" to "スキルスピード",
            "de" to "Schnelligkeit",
            "fr" to "Vivacité"
        ), "Vitality" to mapOf(
            "en" to "Vitality",
            "ja" to "VIT",
            "de" to "Konstitution",
            "fr" to "Vitalité"
        )
    )

    val damageType = mapOf(
        "Magic Damage" to mapOf(
            "en" to "Magic Damage",
            "ja" to "魔法基本性能",
            "de" to "Mag. Basiswert",
            "fr" to "Dégâts magiques"
        ), "Physical Damage" to mapOf(
            "en" to "Physical Damage",
            "ja" to "物理基本性能",
            "de" to "Phys. Basiswert",
            "fr" to "Dégâts physiques"
        )
    )

    val delay = mapOf(
        "en" to "Delay", "ja" to "攻撃間隔", "de" to "Verzögerung", "fr" to "Délai"
    )

    val itemLevel = mapOf(
        "en" to "Item Level",
        "ja" to "ITEM LEVEL",
        "de" to "G.-Stufe",
        "fr" to "Niveau d'objet"
    )

    val level = mapOf(
        "en" to "Lv.", "ja" to "Lv", "de" to "Ab St.", "fr" to "Nv"
    )

    // val magicalDamage: Map<String, String>

    // val physicalDamage: Map<String, String>
}
