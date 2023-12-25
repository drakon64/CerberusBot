package cloud.drakon.dynamisbot.commands

import cloud.drakon.dynamisbot.ephemeral
import cloud.drakon.dynamisbot.lib.discord.applicationcommand.ApplicationCommand
import cloud.drakon.dynamisbot.lib.discord.applicationcommand.ApplicationCommandOption
import cloud.drakon.dynamisbot.lib.discord.applicationcommand.ApplicationCommandOptionChoiceString

private enum class dcRegion {
    datacenter, region
}

internal val universalis = ApplicationCommand(
    type = 1,
    name = "universalis",
    description = "Get Final Fantasy XIV market board listings",
    options = buildList {
        dcRegion.entries.forEach {
            add(
                ApplicationCommandOption(
                    type = 1,
                    name = it.name,
                    description = "Get Final Fantasy XIV market board listings for a $it",
                    options = arrayOf(
                        ApplicationCommandOption(
                            type = 3,
                            name = it.name,
                            description = "The $it to search in",
                            choices = when (it) {
                                dcRegion.region -> buildList {
                                    arrayOf("Japan", "Europe", "North America", "Oceania").forEach {
                                        add(
                                            ApplicationCommandOptionChoiceString(
                                                name = it,
                                                value = it.replace(" ", "-")
                                            )
                                        )
                                    }
                                }.toTypedArray()

                                dcRegion.datacenter -> buildList {
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
                                }.toTypedArray()
                            }
                        ),
                        ApplicationCommandOption(
                            type = 3,
                            name = "item",
                            description = "The item to search for",
                        ),
                        ApplicationCommandOption(
                            type = 5,
                            name = "high_quality",
                            description = "Show only high quality market board listings or vice versa",
                        ),
                        ephemeral,
                    )
                )
            )
        }
    }.toTypedArray()
)
