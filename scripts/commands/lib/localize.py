import boto3

discord_to_aws = (
    {"discord": "de", "aws": "de"},
    {"discord": "fr", "aws": "fr"},
    {"discord": "ja", "aws": "ja"},
)

translate_client = boto3.client(
    service_name="translate", region_name="eu-west-2", use_ssl=True
)

with open("terminology.csv", "rb") as terminology_csv:
    terminology = terminology_csv.read()

translate_client.import_terminology(
    Name="DynamisBot",
    MergeStrategy="OVERWRITE",
    TerminologyData={
        "File": terminology,
        "Format": "CSV",
        "Directionality": "UNI",
    },
)


def translate_text(text: str, target_language_code: str) -> str:
    return translate_client.translate_text(
        Text=text,
        TerminologyNames=("DynamisBot",),
        SourceLanguageCode="en",
        TargetLanguageCode=target_language_code,
        Settings={"Formality": "INFORMAL"},
    ).get("TranslatedText")


def translate_command(command: dict, slash_command=True):
    for target_language in discord_to_aws:
        if "skip_localization" not in command or not command["skip_localization"]:
            command["name_localizations"][target_language["discord"]] = translate_text(
                command["name"], target_language["aws"]
            )

            if slash_command:
                command["name_localizations"][target_language["discord"]] = (
                    command["name_localizations"][target_language["discord"]]
                    .replace(" ", "_")
                    .replace("'", "")
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

    if "skip_localization" in command and command["skip_localization"]:
        del command["skip_localization"]
        del command["name_localizations"]

    if slash_command:
        command["name"] = command["name"].replace(" ", "_").lower()

    return command
