package cloud.drakon.dynamisbot.commands

import cloud.drakon.dynamisbot.ephemeral
import cloud.drakon.dynamisbot.lib.discord.applicationcommand.ApplicationCommand
import cloud.drakon.dynamisbot.lib.discord.applicationcommand.ApplicationCommandOption

internal val universalis = ApplicationCommand(
    type = 1,
    name = "universalis",
    description = "Get Final Fantasy XIV market board listings",
    options = buildList {
        arrayOf("world", "datacenter", "region").forEach {
            add(
                ApplicationCommandOption(
                    type = 1,
                    name = it,
                    description = "Get Final Fantasy XIV market board listings for a $it",
                    options = arrayOf(
                        ApplicationCommandOption(
                            type = 3,
                            name = it,
                            description = "The $it to search in"
                        ),
                        ApplicationCommandOption(
                            type = 3,
                            name = "item",
                            description = "The item to search for"
                        ),
                        ApplicationCommandOption(
                            type = 5,
                            name = "high_quality",
                            description = "Show only high quality market board listings or vice versa"
                        ),
                        ephemeral,
                    )
                )
            )
        }
    }.toTypedArray()
)
