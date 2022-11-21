package cloud.drakon.tempestbot

import aws.sdk.kotlin.services.translate.TranslateClient
import aws.sdk.kotlin.services.translate.model.TranslateTextRequest

suspend fun translate(
    Text: String,
    TargetLanguageCode: String,
    SourceLanguageCode: String? = null,
): String { // TODO Make region configurable
    return TranslateClient { region = "eu-west-2" }.translateText(TranslateTextRequest {
        text = Text
        targetLanguageCode = TargetLanguageCode
        sourceLanguageCode = SourceLanguageCode
    }).translatedText.toString()
}
