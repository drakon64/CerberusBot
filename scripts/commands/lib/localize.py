import boto3

discord_to_aws = (
    {"discord": "de", "aws": "de"},
    {"discord": "fr", "aws": "fr"},
    {"discord": "ja", "aws": "ja"},
)

translate_client = boto3.client(
    service_name="translate", region_name="eu-west-2", use_ssl=True
)


def translate_text(text: str, target_language_code: str) -> str:
    return translate_client.translate_text(
        Text=text, SourceLanguageCode="en", TargetLanguageCode=target_language_code
    ).get("TranslatedText")


def translate_command(command: dict, slash_command=True):
    for target_language in discord_to_aws:
        command["name_localizations"][target_language["discord"]] = translate_text(
            command["name"], target_language["aws"]
        )

        if slash_command:
            command["name_localizations"][target_language["discord"]] = (
                command["name_localizations"][target_language["discord"]]
                .replace(" ", "_")
                .lower()
            )

        if "description" in command:
            command["description_localizations"][target_language["discord"]] = (
                translate_text(command["description"], target_language["aws"])
            )

        if "options" in command:
            for option_or_subcommand in command["options"]:
                option_or_subcommand["name_localizations"][
                    target_language["discord"]
                ] = (
                    translate_text(option_or_subcommand["name"], target_language["aws"])
                    .replace(" ", "_")
                    .lower()
                )
                option_or_subcommand["description_localizations"][
                    target_language["discord"]
                ] = translate_text(
                    option_or_subcommand["description"], target_language["aws"]
                )

                if "choices" in option_or_subcommand:
                    for choice in option_or_subcommand["choices"]:
                        choice["name_localizations"][target_language["discord"]] = (
                            translate_text(choice["name"], target_language["aws"])
                        )

                if "options" in option_or_subcommand:
                    for option in option_or_subcommand["options"]:
                        option["name_localizations"][target_language["discord"]] = (
                            translate_text(
                                option["name"], target_language["aws"]
                            ).replace(" ", "_")
                        ).lower()
                        option["description_localizations"][
                            target_language["discord"]
                        ] = translate_text(
                            option["description"], target_language["aws"]
                        )

                        if "choices" in option:
                            for choice in option["choices"]:
                                choice["name_localizations"][
                                    target_language["discord"]
                                ] = translate_text(
                                    choice["name"], target_language["aws"]
                                )

                option_or_subcommand["name"] = (
                    option_or_subcommand["name"].replace(" ", "_").lower()
                )

    if slash_command:
        command["name"] = command["name"].replace(" ", "_").lower()

    return command
