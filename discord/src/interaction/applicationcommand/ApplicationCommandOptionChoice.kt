package cloud.drakon.dynamisbot.discord.interaction.applicationcommand

import cloud.drakon.dynamisbot.discord.Locale
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// https://discord.com/developers/docs/interactions/application-commands#application-command-object-application-command-option-choice-structure

@Deprecated("Will be replaced with a class where the `value` parameter is a Union of String, Int, Double, and Boolean")
@Serializable
sealed interface ApplicationCommandOptionChoice {
    val name: String
    val nameLocalizations: Map<Locale, String>?
}

@Serializable
class ApplicationCommandOptionChoiceString(
    override val name: String,
    @SerialName("name_localizations") override val nameLocalizations: Map<Locale, String>? = null,
    val value: String, // TODO: Should be a Union of String, Int, Double, and Boolean
) : ApplicationCommandOptionChoice
