package cloud.drakon.dynamisbot.lib.discord.applicationcommand

import cloud.drakon.dynamisbot.lib.discord.Locale
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// https://discord.com/developers/docs/interactions/application-commands#application-command-object-application-command-option-choice-structure

@Deprecated("// TODO: `value` should be a Union of String, Int, Double, and Boolean")
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

@Serializable
internal class ApplicationCommandOptionChoiceInt(
    override val name: String,
    @SerialName("name_localizations") override val nameLocalizations: Map<Locale, String>? = null,
    val value: Int // TODO: Should be a Union of String, Int, Double, and Boolean
) : ApplicationCommandOptionChoice

@Serializable
internal class ApplicationCommandOptionChoiceDouble(
    override val name: String,
    @SerialName("name_localizations") override val nameLocalizations: Map<Locale, String>? = null,
    val value: Double // TODO: Should be a Union of String, Int, Double, and Boolean
) : ApplicationCommandOptionChoice

@Serializable
internal class ApplicationCommandOptionChoiceBoolean(
    override val name: String,
    @SerialName("name_localizations") override val nameLocalizations: Map<Locale, String>? = null,
    val value: Boolean // TODO: Should be a Union of String, Int, Double, and Boolean
) : ApplicationCommandOptionChoice
