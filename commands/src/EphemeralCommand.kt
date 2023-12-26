package cloud.drakon.dynamisbot

import cloud.drakon.dynamisbot.lib.discord.applicationcommand.ApplicationCommandOption
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

internal suspend fun ephemeralCommand(): ApplicationCommandOption = coroutineScope {
    val name = "Ephemeral"
    val description = "Make the bot response visible to you only"

    val nameLocalizations = async {
        name.buildLocalizationMap(true)
    }
    val descriptionLocalizations = async {
        description.buildLocalizationMap()
    }

    return@coroutineScope ApplicationCommandOption(
        type = 5,
        name = name.commandName(),
        nameLocalizations = nameLocalizations.await(),
        description = description,
        descriptionLocalizations = descriptionLocalizations.await(),
    )
}
