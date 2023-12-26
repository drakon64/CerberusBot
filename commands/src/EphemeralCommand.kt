package cloud.drakon.dynamisbot

import cloud.drakon.dynamisbot.lib.discord.applicationcommand.ApplicationCommandOption

internal suspend fun ephemeralCommand(): ApplicationCommandOption {
    val name = "ephemeral"
    val description = "Make the bot response visible to you only"

    return ApplicationCommandOption(
        type = 5,
        name = name,
        nameLocalizations = name.buildLocalizationMap(true),
        description = description,
        descriptionLocalizations = description.buildLocalizationMap(),
    )
}
