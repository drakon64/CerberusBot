from lib.localize import translate_command
from option.ephemeral import ephemeral


async def create_universalis_command():
    command = {
        "name": "universalis",
        "name_localizations": {},
        "description": "Get Final Fantasy XIV market board listings",
        "description_localizations": {},
        "type": 1,
        "options": [
            {
                "name": "item",
                "name_localizations": {},
                "description": "The item to search for",
                "description_localizations": {},
                "required": True,
                "type": 3,
            },
            {
                "name": "world",
                "name_localizations": {},
                "description": "The World/Data Center/Region to search",
                "description_localizations": {},
                "required": True,
                "type": 3,
            },
            {
                "name": "high quality",
                "name_localizations": {},
                "description": (
                    "Only show high quality market board listings or vice versa"
                ),
                "description_localizations": {},
                "type": 5,
            },
            ephemeral,
        ],
    }

    return translate_command(command)
