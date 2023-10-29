package cloud.drakon.dynamisbot.eorzeadatabase.item

internal object Localisation {
    val autoAttack = mapOf(
        "ja" to "物理オートアタック",
        "de" to "Auto-Attacke",
        "fr" to "Attaque auto."
    ).withDefault { "Auto-attack" }

    val baseItemLevel = mapOf(
        "baseItem" to mapOf(
            "ja" to "装着可能な装備", "de" to "Einsetzbar in", "fr" to "Sertissable"
        ).withDefault { "Base Item:" }, "itemLevel" to mapOf(
            "ja" to "ITEM LEVEL", "de" to "Gegenstände ab Stufe", "fr" to "Niveau d'objet"
        ).withDefault { "Item Level" }
    )

    val block = mapOf(
        "Strength" to mapOf(
            "ja" to "ブロック性能",
            "de" to "Blockeffekt",
            "fr" to "Force de blocage"
        ).withDefault { "Block Strength" },
        "Rate" to mapOf(
            "ja" to "ブロック発動力",
            "de" to "Blockrate",
            "fr" to "Taux de blocage"
        ).withDefault { "Block Rate" }
    )

    val bonuses = mapOf(
        "Bonuses" to mapOf(
            "de" to "Bonus",
            "fr" to "Bonus"
        ).withDefault { "Bonuses" },
        "Control" to mapOf(
            "ja" to "加工精度",
            "de" to "Kontrolle",
            "fr" to "Contrôle"
        ).withDefault { "Control" },
        "CP" to mapOf(
            "de" to "HP",
            "fr" to "PS"
        ).withDefault { "CP" },
        "Craftsmanship" to mapOf(
            "ja" to "作業精度",
            "de" to "Kunstfertigkeit",
            "fr" to "Habileté"
        ).withDefault { "Craftsmanship" },
        "CriticalHit" to mapOf(
            "ja" to "クリティカル",
            "de" to "Kritischer Treffer",
            "fr" to "Critique"
        ).withDefault { "Critical Hit" },
        "Determination" to mapOf(
            "ja" to "意思力",
            "de" to "Entschlossenheit",
            "fr" to "Détermination"
        ).withDefault { "Determination" },
        "DirectHitRate" to mapOf(
            "ja" to "ダイレクトヒット",
            "de" to "Direkter Treffer",
            "fr" to "Coups nets"
        ).withDefault { "Direct Hit Rate" },
        "Gathering" to mapOf(
            "ja" to "獲得力",
            "de" to "Sammelgeschick",
            "fr" to "Collecte",
        ).withDefault { "Gathering" },
        "GP" to mapOf(
            "de" to "SP",
            "fr" to "PR"
        ).withDefault { "GP" },
        "Intelligence" to mapOf(
            "ja" to "INT",
            "de" to "Intelligenz",
        ).withDefault { "Intelligence" },
        "Perception" to mapOf(
            "ja" to "技術力",
            "de" to "Expertise",
            "fr" to "Savoir-faire"
        ).withDefault { "Perception" },
        "Piety" to mapOf(
            "ja" to "信仰",
            "de" to "Frömmigkeit",
            "fr" to "Piété"
        ).withDefault { "Piety" },
        "SkillSpeed" to mapOf(
            "ja" to "スキルスピード",
            "de" to "Schnelligkeit",
            "fr" to "Vivacité"
        ).withDefault { "Skill Speed" },
        "SpellSpeed" to mapOf(
            "ja" to "スペルスピード",
            "de" to "Zaubertempo",
            "fr" to "Célérité"
        ).withDefault { "Spell Speed" },
        "Strength" to mapOf(
            "ja" to "STR",
            "de" to "Stärke",
            "fr" to "Force"
        ).withDefault { "Strength" },
        "Tenacity" to mapOf(
            "ja" to "不屈",
            "de" to "Unbeugsamkeit",
            "fr" to "Ténacité"
        ).withDefault { "Tenacity" },
        "Vitality" to mapOf(
            "ja" to "VIT",
            "de" to "Konstitution",
            "fr" to "Vitalité"
        ).withDefault { "Vitality" }
    )

    val damageType = mapOf(
        "Magic Damage" to mapOf(
            "ja" to "魔法基本性能",
            "de" to "Mag. Basiswert",
            "fr" to "Dégâts magiques"
        ).withDefault { "Magic Damage" },
        "Physical Damage" to mapOf(
            "ja" to "物理基本性能",
            "de" to "Phys. Basiswert",
            "fr" to "Dégâts physiques"
        ).withDefault { "Physical Damage" }
    )

    val defense = mapOf(
        "Defense" to mapOf(
            "ja" to "物理防御力",
            "de" to "Verteidigung",
            "fr" to "Défense"
        ).withDefault { "Defense" },
        "Magic Defense" to mapOf(
            "ja" to "魔法防御力",
            "de" to "Magieabwehr",
            "fr" to "Défense magique"
        ).withDefault { "Magic Defense" }
    )

    val delay = mapOf(
        "ja" to "攻撃間隔",
        "de" to "Verzögerung",
        "fr" to "Délai"
    ).withDefault { "Delay" }

    val effects = mapOf(
        "de" to "Effekt", "fr" to "Effets"
    ).withDefault { "Effects" }

    val itemLevel = mapOf(
        "ja" to "ITEM LEVEL",
        "de" to "G.-Stufe",
        "fr" to "Niveau d'objet"
    ).withDefault { "Item Level" }

    val level = mapOf(
        "ja" to "Lv", "de" to "Ab St.", "fr" to "Nv"
    ).withDefault { "Lv." }

    val requirements = mapOf(
        "ja" to "マテリア装着", "de" to "Materia einsetzen", "fr" to "Sertissage"
    ).withDefault { "Requirements" }
}
