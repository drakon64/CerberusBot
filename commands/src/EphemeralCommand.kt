package cloud.drakon.dynamisbot

import cloud.drakon.dynamisbot.lib.discord.applicationcommand.ApplicationCommandOption

internal suspend fun ephemeralCommand(): ApplicationCommandOption {
    val name = "Ephemeral"
    val description = "Make the bot response visible to you only"

    return ApplicationCommandOption(
        type = 5,
        name = name.commandName(),
        nameLocalizations = name.buildLocalizationMap(true),
        description = description,
        descriptionLocalizations = description.buildLocalizationMap(),
    )
}
