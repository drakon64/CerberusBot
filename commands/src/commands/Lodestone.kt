package cloud.drakon.dynamisbot.commands

import cloud.drakon.dynamisbot.buildLocalizationMap
import cloud.drakon.dynamisbot.commandName
import cloud.drakon.dynamisbot.discord.Locale
import cloud.drakon.dynamisbot.discord.interaction.applicationcommand.ApplicationCommand
import cloud.drakon.dynamisbot.discord.interaction.applicationcommand.ApplicationCommandOption

internal suspend fun lodestoneCommand() = ApplicationCommand(
    type = 1,
    name = "The Lodestone".commandName(),
    nameLocalizations = mapOf(
        Locale.German to "Der Lodestone".commandName(),
    ),
    description = "FINAL FANTASY XIV, The Lodestone",
    descriptionLocalizations = mapOf(
        Locale.German to "FINAL FANTASY XIV - Der Lodestone",
        Locale.French to "FINAL FANTASY XIV : The Lodestone",
    ),
    options = buildList {
        var name = "Link"
        var description = "Link your Discord account to a Final Fantasy XIV character"

        add(
            ApplicationCommandOption(
                type = 1,
                name = name.commandName(),
                nameLocalizations = name.buildLocalizationMap(true),
                description = description,
                descriptionLocalizations = description.buildLocalizationMap(),
                options = buildList {
                    name = "Character"
                    description = "The name of the character to link to"

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

                    name = "World"
                    description = "The world of the character to link to"

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

                    add(ephemeralOption())
                }
            )
        )

        name = "Unlink"
        description = "Unlink your Discord account from a Final Fantasy XIV character"

        add(
            ApplicationCommandOption(
                type = 1,
                name = name.commandName(),
                nameLocalizations = name.buildLocalizationMap(true),
                description = description,
                descriptionLocalizations = description.buildLocalizationMap(),
                options = buildList {
                    name = "Global"
                    description = "Unlink from all Discord guilds"

                    add(
                        ApplicationCommandOption(
                            type = 5,
                            name = name.commandName(),
                            nameLocalizations = name.buildLocalizationMap(true),
                            description = description,
                            descriptionLocalizations = description.buildLocalizationMap(),
                        )
                    )

                    add(ephemeralOption())
                }
            )
        )

        name = "Card"
        description = "Get a character card of a users Final Fantasy XIV character"

        add(
            ApplicationCommandOption(
                type = 1,
                name = name.commandName(),
                nameLocalizations = name.buildLocalizationMap(true),
                description = description,
                descriptionLocalizations = description.buildLocalizationMap(),
                options = buildList {
                    name = "User"
                    description = "The user to get a character card from"

                    add(
                        ApplicationCommandOption(
                            type = 6,
                            name = name.commandName(),
                            nameLocalizations = name.buildLocalizationMap(true),
                            description = description,
                            descriptionLocalizations = description.buildLocalizationMap(),
                            required = true,
                        )
                    )

                    add(ephemeralOption())
                }
            )
        )

        name = "Portrait"
        description = "Get a portrait of a users Final Fantasy XIV character"

        add(
            ApplicationCommandOption(
                type = 1,
                name = name.commandName(),
                nameLocalizations = name.buildLocalizationMap(true),
                description = description,
                descriptionLocalizations = description.buildLocalizationMap(),
                options = buildList {
                    name = "User"
                    description = "The user to get a character portrait from"

                    add(
                        ApplicationCommandOption(
                            type = 6,
                            name = name.commandName(),
                            nameLocalizations = name.buildLocalizationMap(true),
                            description = description,
                            descriptionLocalizations = description.buildLocalizationMap(),
                            required = true,
                        )
                    )

                    add(ephemeralOption())
                }
            )
        )
    }
)
