import json
import os

import requests

from localizations import languages, translate_text

application_commands = (
    {
        "name": "citation",
        "description": "Citations",
        "default_member_permissions": 0,
        "dm_permission": False,
        "type": 1,
        "options": (
            {
                "name": "get",
                "description": "Get citations",
                "type": 1,
                "options": (
                    {
                        "name": "user",
                        "description": "The user to get a random citation from",
                        "type": 6,
                        "required": True,
                    },
                ),
            },
            {
                "name": "add",
                "description": "Add citations",
                "type": 1,
                "options": (
                    {
                        "name": "user",
                        "description": "The user to add a citation to",
                        "type": 6,
                        "required": True,
                    },
                    {
                        "name": "citation",
                        "description": "The citation to add",
                        "type": 3,
                        "required": True,
                    },
                ),
            },
            {"name": "opt-in", "description": "Opt-in to citations", "type": 1},
            {"name": "opt-out", "description": "Opt-out of citations", "type": 1},
        ),
    },
    {
        "name": "Add citation",
        "default_member_permissions": 0,
        "dm_permission": False,
        "type": 3,
    },
    {
        "name": "Get citation",
        "default_member_permissions": 0,
        "dm_permission": False,
        "type": 2,
    },
    {
        "name": "lodestone",
        "description": "Final Fantasy XIV, The Lodestone",
        "default_member_permissions": 0,
        "dm_permission": False,
        "type": 1,
        "options": (
            {
                "name": "card",
                "description": (
                    "Get a character card of a users Final Fantasy XIV character"
                ),
                "type": 1,
                "options": (
                    {
                        "name": "user",
                        "description": "The user to get a Lodestone card for",
                        "type": 6,
                        "required": True,
                    },
                ),
            },
            {
                "name": "link",
                "description": (
                    "Link your Discord account to a Final Fantasy XIV character"
                ),
                "type": 1,
                "options": (
                    {
                        "name": "character",
                        "description": (
                            "The name of the character to link your Discord account to"
                        ),
                        "type": 3,
                        "required": True,
                    },
                    {
                        "name": "world",
                        "description": (
                            "The world of the character to link your Discord account to"
                        ),
                        "type": 3,
                        "required": True,
                    },
                ),
            },
            {
                "name": "unlink",
                "description": (
                    "Unlink your Discord account from a Final Fantasy XIV character"
                ),
                "type": 1,
            },
            {
                "name": "portrait",
                "description": "Get a portrait of a users Final Fantasy XIV character",
                "type": 1,
                "options": (
                    {
                        "name": "user",
                        "description": "The user to get a Lodestone portrait for",
                        "type": 6,
                        "required": True,
                    },
                ),
            },
            {
                "name": "profile",
                "description": "Get a profile of a users Final Fantasy XIV character",
                "type": 1,
                "options": (
                    {
                        "name": "user",
                        "description": "The user to get a Lodestone profile for",
                        "type": 6,
                        "required": True,
                    },
                ),
            },
        ),
    },
    {
        "name": "rory",
        "description": "cat",
        "default_member_permissions": 0,
        "type": 1,
        "options": (
            {
                "name": "id",
                "description": "Rory ID",
                "type": 4,
                "max_value": 123,
                "required": False,
            },
        ),
    },
    {
        "name": "translate",
        "description": "Translate to or from other languages",
        "default_member_permissions": 0,
        "type": 1,
        "options": (
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
        ),
    },
    {"name": "Translate", "default_member_permissions": 0, "type": 3},
    {
        "name": "universalis",
        "description": "Get Final Fantasy XIV market board item information",
        "default_member_permissions": 0,
        "type": 1,
        "options": (
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
                "description": "Only show high quality item listings or vice versa",
                "type": 5,
                "required": False,
            },
        ),
    },
)

application_id = os.environ["APPLICATION_ID"]
bot_token = os.environ["BOT_TOKEN"]

for command in application_commands:
    if command["type"] == 1:
        command["name"] = command["name"].replace(" ", "_")

    name_localizations = {}
    description_localizations = {}
    for language in languages:
        if command["type"] == 1:
            name_localizations.update(
                {
                    language["discord"]: (
                        translate_text(command["name"], language["aws"])
                        .replace(" ", "_")
                        .replace("...", "")
                        .lower()
                    )
                }
            )
        else:
            name_localizations.update(
                {
                    language["discord"]: translate_text(
                        command["name"], language["aws"]
                    ).replace("...", "")
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

    if "description" in command:
        command["description_localizations"] = description_localizations

    if "options" in command:
        for sub_command_or_group in command["options"]:
            name_localizations = {}
            description_localizations = {}

            for language in languages:
                name_localizations.update(
                    {
                        language["discord"]: (
                            translate_text(
                                sub_command_or_group["name"], language["aws"]
                            )
                            .replace(" ", "_")
                            .replace("...", "")
                            .replace("'", "")
                            .lower()
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
            sub_command_or_group["name"] = sub_command_or_group["name"].replace(
                " ", "_"
            )

            if "description" in sub_command_or_group:
                sub_command_or_group["description_localizations"] = (
                    description_localizations
                )

            if "options" in sub_command_or_group:
                for sub_command in sub_command_or_group["options"]:
                    name_localizations = {}
                    description_localizations = {}

                    for language in languages:
                        name_localizations.update(
                            {
                                language["discord"]: (
                                    translate_text(sub_command["name"], language["aws"])
                                    .replace(" ", "_")
                                    .replace("...", "")
                                    .lower()
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
                    sub_command["name"] = sub_command["name"].replace(" ", "_")

                    if "description" in sub_command:
                        sub_command["description_localizations"] = (
                            description_localizations
                        )

# json.dump(application_commands, open("discord.json", "w"), indent=4)
print(
    json.dumps(
        json.loads(
            bytes.decode(
                requests.put(
                    f"https://discord.com/api/v10/applications/{application_id}/commands",
                    headers={
                        "Authorization": f"Bot {bot_token}",
                        "Content-Type": "application/json",
                    },
                    data=json.dumps(application_commands),
                ).content
            )
        ),
        indent=4,
    )
)
