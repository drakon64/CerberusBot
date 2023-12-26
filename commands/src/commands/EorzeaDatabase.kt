package cloud.drakon.dynamisbot.commands

import cloud.drakon.dynamisbot.buildLocalizationMap
import cloud.drakon.dynamisbot.commandName
import cloud.drakon.dynamisbot.ephemeralCommand
import cloud.drakon.dynamisbot.lib.discord.applicationcommand.ApplicationCommand
import cloud.drakon.dynamisbot.lib.discord.applicationcommand.ApplicationCommandOption
import cloud.drakon.dynamisbot.lib.discord.applicationcommand.ApplicationCommandOptionChoiceString

private const val name = "Eorzea Database"
private const val description = "Search the Eorzea Database"

internal suspend fun eorzeaDatabaseCommand() = ApplicationCommand(
    type = 1,
    name = name.commandName(),
//    nameLocalizations = name.buildLocalizationMap(true),
    description = description,
    descriptionLocalizations = description.buildLocalizationMap(),
    options = buildList {
        var name = "Index"
        var description = "The index to search in"

        add(
            ApplicationCommandOption(
                type = 3,
                name = name.commandName(),
                nameLocalizations = name.buildLocalizationMap(true),
                description = description,
                descriptionLocalizations = description.buildLocalizationMap(),
                required = true,
                choices = buildList {
                    var name = "Item"

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
                                name = it.commandName(),
                                nameLocalizations = it.buildLocalizationMap(),
                                value = it
                            )
                        )
                    }
                }
            )
        )

        add(ephemeralCommand())
    }
)
