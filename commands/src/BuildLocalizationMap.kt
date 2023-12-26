package cloud.drakon.dynamisbot

import aws.sdk.kotlin.services.translate.TranslateClient
import aws.sdk.kotlin.services.translate.model.Formality
import aws.sdk.kotlin.services.translate.model.TranslateTextRequest
import cloud.drakon.dynamisbot.lib.discord.Locale

internal suspend fun buildLocalizationMap(translateText: String, isName: Boolean = false) = buildMap {
    Locale.entries.forEach { locale ->
        put(locale, TranslateClient.fromEnvironment().use {
            it.translateText(TranslateTextRequest {
                sourceLanguageCode = "en"
                targetLanguageCode = locale.aws

                settings {
                    formality = Formality.Informal
                }

                text = translateText
            }).translatedText.let {
                if (isName) {
                    it.lowercase().trim().replace(" ", "_")
                } else it
            }
        })
    }
}
