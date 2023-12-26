package cloud.drakon.dynamisbot.commands

import cloud.drakon.dynamisbot.buildLocalizationMap
import cloud.drakon.dynamisbot.commandName
import cloud.drakon.dynamisbot.ephemeralCommand
import cloud.drakon.dynamisbot.lib.discord.applicationcommand.ApplicationCommand
import cloud.drakon.dynamisbot.lib.discord.applicationcommand.ApplicationCommandOption
import cloud.drakon.dynamisbot.lib.discord.applicationcommand.ApplicationCommandOptionChoiceString

private const val name = "Universalis"
private const val description = "Get Final Fantasy XIV market board listings"

internal suspend fun universalisCommand() = ApplicationCommand(
    type = 1,
    name = name.commandName(),
    nameLocalizations = name.buildLocalizationMap(true),
    description = description,
    descriptionLocalizations = description.buildLocalizationMap(),
    options = buildList {
        arrayOf("datacenter", "region").forEach {
            val name = it.commandName()
            val description = "Get Final Fantasy XIV market board listings for a $it"

            add(
                ApplicationCommandOption(
                    type = 1,
                    name = name,
                    nameLocalizations = it.buildLocalizationMap(true),
                    description = description,
                    descriptionLocalizations = description.buildLocalizationMap(),
                    options = buildList {
                        var description = "The $it to search in"

                        add(
                            ApplicationCommandOption(
                                type = 3,
                                name = name,
                                nameLocalizations = it.buildLocalizationMap(true),
                                description = description,
                                descriptionLocalizations = description.buildLocalizationMap(),
                                choices = when (it) {
                                    "datacenter" -> buildList {
                                        arrayOf(
                                            "Elemental",
                                            "Gaia",
                                            "Mana",
                                            "Meteor",
                                            "Chaos",
                                            "Light",
                                            "Aether",
                                            "Crystal",
                                            "Dynamis",
                                            "Primal",
                                            "Materia",
                                            "NA Cloud DC (Beta)",
                                        ).forEach {
                                            add(ApplicationCommandOptionChoiceString(name = it, value = it))
                                        }
                                    }

                                    "region" -> buildList {
                                        arrayOf("Japan", "Europe", "North America", "Oceania").forEach {
                                            add(
                                                ApplicationCommandOptionChoiceString(
                                                    name = it,
                                                    value = it.replace(" ", "-")
                                                )
                                            )
                                        }
                                    }

                                    else -> null
                                }
                            )
                        )

                        var name = "item"
                        description = "The item to search for"

                        add(
                            ApplicationCommandOption(
                                type = 3,
                                name = name.commandName(),
                                nameLocalizations = name.buildLocalizationMap(true),
                                description = description,
                                descriptionLocalizations = description.buildLocalizationMap(),
                            )
                        )

                        name = "High Quality"
                        description = "Show only High Quality market board listings or vice versa"

                        add(
                            ApplicationCommandOption(
                                type = 5,
                                name = name.commandName(),
                                nameLocalizations = name.buildLocalizationMap(true),
                                description = description,
                                descriptionLocalizations = description.buildLocalizationMap(),
                            )
                        )
                        add(ephemeralCommand())
                    }
                )
            )
        }
    }
)
