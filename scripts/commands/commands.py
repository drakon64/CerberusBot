import json

import requests

commands = json.load(open("commands.json", "r"))

application_commands = (
    {
        "name": "translate",
        "description": "Translate to or from other languages",
        "default_member_permissions": 0,
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

commands_put = requests.put(
    f"https://discord.com/api/v10/applications/{application_id}/commands",
    headers={"Authorization": f"Bot {bot_token}"},
    json=application_commands,
)
print(commands_put.status_code, commands_put.content)
