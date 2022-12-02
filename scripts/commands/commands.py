import json

import requests

from localizations import languages, translate_text

commands = json.load(open("commands.json", "r"))

application_commands = (
    {
        "name": "translate",
        "description": "Translate to or from other languages",
        "default_member_permissions": 0,
        "type": 1,
        "options": [
            {
                "name": "message",
                "description": "The text to translate",
                "type": 3,
                "required": True,
            },
            {
                "name": "to",
                "description": "The language to translate to",
                "type": 3,
                "required": True,
            },
            {
                "name": "from",
                "description": "The language to translate from",
                "type": 3,
                "required": False,
            },
        ],
    },
    {
        "name": "Translate",
        "default_member_permissions": 0,
        "dm_permission": False,
        "type": 3,
    },
)

application_id = commands["application_id"]
bot_token = commands["bot_token"]

for command in application_commands:
    name_localizations = {}
    description_localizations = {}
    for language in languages:
        name_localizations.update(
            {language["discord"]: translate_text(command["name"], language["aws"])}
        )
        if "description" in command:
            description_localizations.update(
                {
                    language["discord"]: translate_text(
                        command["description"], language["aws"]
                    )
                }
            )
    command["name_localizations"] = name_localizations

    if "description" in command:
        command["description_localizations"] = description_localizations

    if "options" in command:
        for sub_command_or_group in command["options"]:
            name_localizations = {}
            description_localizations = {}

            for language in languages:
                name_localizations.update(
                    {
                        language["discord"]: translate_text(
                            sub_command_or_group["name"], language["aws"]
                        )
                    }
                )
                if "description" in sub_command_or_group:
                    description_localizations.update(
                        {
                            language["discord"]: translate_text(
                                sub_command_or_group["description"], language["aws"]
                            )
                        }
                    )
            sub_command_or_group["name_localizations"] = name_localizations

            if "description" in sub_command_or_group:
                sub_command_or_group[
                    "description_localizations"
                ] = description_localizations

        if "options" in sub_command_or_group:
            for sub_command in sub_command_or_group["options"]:
                name_localizations = {}
                description_localizations = {}

                for language in languages:
                    name_localizations.update(
                        {
                            language["discord"]: translate_text(
                                sub_command["name"], language["aws"]
                            )
                        }
                    )
                    if "description" in sub_command:
                        description_localizations.update(
                            {
                                language["discord"]: translate_text(
                                    sub_command["description"], language["aws"]
                                )
                            }
                        )
                sub_command["name_localizations"] = name_localizations

                if "description" in sub_command:
                    sub_command["description_localizations"] = description_localizations

print(json.dumps(application_commands, indent=4))

commands_put = requests.put(
    f"https://discord.com/api/v10/applications/{application_id}/commands",
    headers={"Authorization": f"Bot {bot_token}"},
    json=application_commands,
)
print(commands_put.status_code, commands_put.content)
