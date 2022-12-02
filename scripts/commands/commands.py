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
    {
        "name": "universalis",
        "description": "Get Final Fantasy XIV market board item information",
        "default_member_permissions": 0,
        "type": 1,
        "options": [
            {
                "name": "item",
                "description": "The item to search for",
                "type": 3,
                "required": True,
            },
            {
                "name": "world",
                "description": "The World/Data Center/Region to search",
                "type": 3,
                "required": True,
            },
            {
                "name": "high quality",
                "description": "Only show high quality items",
                "type": 5,
                "required": False,
            },
        ],
    },
)

application_id = commands["application_id"]
bot_token = commands["bot_token"]

for command in application_commands:
    name_localizations = {}
    description_localizations = {}
    for language in languages:
        if command["type"] == 1:
            name_localizations.update(
                {
                    language["discord"]: translate_text(
                        command["name"], language["aws"]
                    )
                    .replace(" ", "_")
                    .replace("...", "")
                    .lower()
                }
            )
        else:
            name_localizations.update(
                {
                    language["discord"]: translate_text(
                        command["name"], language["aws"]
                    )
                    .replace(" ", "_")
                    .replace("...", "")
                }
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
    command["name"] = command["name"].replace(" ", "_")

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
                        .replace(" ", "_")
                        .replace("...", "")
                        .lower()
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
            sub_command_or_group["name"] = sub_command_or_group["name"].replace(
                " ", "_"
            )

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
                            .replace(" ", "_")
                            .replace("...", "")
                            .lower()
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
                sub_command["name"] = sub_command["name"].replace(" ", "_")

                if "description" in sub_command:
                    sub_command["description_localizations"] = description_localizations

commands_put = requests.put(
    f"https://discord.com/api/v10/applications/{application_id}/commands",
    headers={"Authorization": f"Bot {bot_token}", "Content-Type": "application/json"},
    data=json.dumps(application_commands, ensure_ascii=False).encode("utf-8"),
)
discord = json.loads(bytes.decode(commands_put.content))
print(json.dumps(discord, indent=4))
