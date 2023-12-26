package cloud.drakon.dynamisbot.lib.discord.applicationcommand

import cloud.drakon.dynamisbot.lib.discord.Locale
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// https://discord.com/developers/docs/interactions/application-commands#application-command-object-application-command-option-choice-structure

@Deprecated("Will be replaced with a class where the `value` parameter is a Union of String, Int, Double, and Boolean")
@Serializable
internal sealed interface ApplicationCommandOptionChoice {
    val name: String
    val nameLocalizations: Map<Locale, String>?
}

@Serializable
internal class ApplicationCommandOptionChoiceString(
    override val name: String,
    @SerialName("name_localizations") override val nameLocalizations: Map<Locale, String>? = null,
    val value: String // TODO: Should be a Union of String, Int, Double, and Boolean
) : ApplicationCommandOptionChoice
