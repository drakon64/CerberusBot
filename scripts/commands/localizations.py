import boto3

# Map of Discord locales to AWS Translate language codes
# https://discord.com/developers/docs/reference#locales
# https://docs.aws.amazon.com/translate/latest/dg/what-is-languages.html
languages = (
    {"discord": "da", "aws": "da"},
    {"discord": "de", "aws": "de"},
    {"discord": "en-GB", "aws": "en"},
    {"discord": "en-US", "aws": "en"},
    {"discord": "es-ES", "aws": "es"},
    {"discord": "fr", "aws": "fr"},
    {"discord": "hr", "aws": "hr"},
    {"discord": "it", "aws": "it"},
    {"discord": "lt", "aws": "lt"},
    {"discord": "hu", "aws": "hu"},
    {"discord": "nl", "aws": "nl"},
    {"discord": "no", "aws": "no"},
    {"discord": "pl", "aws": "pl"},
    {"discord": "pt-BR", "aws": "pt"},
    {"discord": "ro", "aws": "ro"},
    {"discord": "fi", "aws": "fi"},
    {"discord": "sv-SE", "aws": "sv"},
    {"discord": "vi", "aws": "vi"},
    {"discord": "tr", "aws": "tr"},
    {"discord": "cs", "aws": "cs"},
    {"discord": "el", "aws": "el"},
    {"discord": "bg", "aws": "bg"},
    {"discord": "ru", "aws": "ru"},
    {"discord": "uk", "aws": "uk"},
    # {"discord": "hi", "aws": "hi"}, # Adds whitespace when it shouldn't
    {"discord": "th", "aws": "th"},
    {"discord": "zh-CN", "aws": "zh"},
    {"discord": "ja", "aws": "ja"},
    {"discord": "zh-TW", "aws": "zh-TW"},
    {"discord": "ko", "aws": "ko"},
)

translate_client = boto3.client(
    service_name="translate", region_name="eu-west-2", use_ssl=True
)


def translate_text(text: str, target_language_code: str) -> str:
    return translate_client.translate_text(
        Text=text,
        SourceLanguageCode="en",
        TargetLanguageCode=target_language_code,
    ).get("TranslatedText")
