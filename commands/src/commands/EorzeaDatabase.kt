package cloud.drakon.dynamisbot.commands

import cloud.drakon.dynamisbot.buildLocalizationMap
import cloud.drakon.dynamisbot.commandName
import cloud.drakon.dynamisbot.discord.Locale
import cloud.drakon.dynamisbot.discord.interaction.applicationcommand.ApplicationCommand
import cloud.drakon.dynamisbot.discord.interaction.applicationcommand.ApplicationCommandOption
import cloud.drakon.dynamisbot.discord.interaction.applicationcommand.ApplicationCommandOptionChoiceString

private var description = "Search the Eorzea Database"

internal suspend fun eorzeaDatabaseCommand() = ApplicationCommand(
    type = 1,
    name = "Eorzea Database".commandName(),
    nameLocalizations = mapOf(
        Locale.Japanese to "エオルゼアデータベース".commandName(),
        Locale.German to "Eorzea-Datenbank".commandName(),
        Locale.French to "Base de données d'Éorzéa".commandName(),
    ),
    description = description,
    descriptionLocalizations = description.buildLocalizationMap(),
    options = buildList {
        var name = "Index"
        description = "The index to search in"

        add(
            ApplicationCommandOption(
                type = 3,
                name = name.commandName(),
                nameLocalizations = name.buildLocalizationMap(true),
                description = description,
                descriptionLocalizations = description.buildLocalizationMap(),
                required = true,
                choices = buildList {
                    name = "Item"

                    add(
                        ApplicationCommandOptionChoiceString(
                            name = name,
                            nameLocalizations = name.buildLocalizationMap(),
                            value = "Item"
                        )
                    )

                    name = "Quest"

                    add(
                        ApplicationCommandOptionChoiceString(
                            name = name,
                            nameLocalizations = name.buildLocalizationMap(),
                            value = "Quest"
                        )
                    )
                }
            )
        )

        name = "Query"
        description = "The search query"

        add(
            ApplicationCommandOption(
                type = 3,
                name = name.commandName(),
                nameLocalizations = name.buildLocalizationMap(true),
                description = description,
                descriptionLocalizations = description.buildLocalizationMap(),
                required = true,
            )
        )

        name = "Language"
        description = "The language to search in"

        add(
            ApplicationCommandOption(
                type = 3,
                name = name.commandName(),
                nameLocalizations = name.buildLocalizationMap(true),
                description = description,
                descriptionLocalizations = description.buildLocalizationMap(),
                choices = buildList {
                    arrayOf("Japanese", "English", "German", "French").forEach {
                        add(
                            ApplicationCommandOptionChoiceString(
                                name = it,
                                nameLocalizations = it.buildLocalizationMap(),
                                value = it
                            )
                        )
                    }
                }
            )
        )

        add(ephemeralOption())
    }
)
