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

for i in application_commands:
    name_localizations = {}
    description_localizations = {}
    for j in languages:
        name_localizations.update({j["discord"]: translate_text(i["name"], j["aws"])})
        if "description" in i:
            description_localizations.update(
                {j["discord"]: translate_text(i["description"], j["aws"])}
            )

    i["name_localizations"] = name_localizations
    if "description" in i:
        i["description_localizations"] = description_localizations

print(json.dumps(application_commands, indent=4))

commands_put = requests.put(
    f"https://discord.com/api/v10/applications/{application_id}/commands",
    headers={"Authorization": f"Bot {bot_token}"},
    json=application_commands,
)
print(commands_put.status_code, commands_put.content)
