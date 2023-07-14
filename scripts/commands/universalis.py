from localize import translate_command


def create_universalis_command():
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
                "type": 3,
                "required": True,
            },
            {
                "name": "world",
                "name_localizations": {},
                "description": "The World/Data Center/Region to search",
                "description_localizations": {},
                "type": 3,
                "required": True,
            },
            {
                "name": "high quality",
                "name_localizations": {},
                "description": "Only show high quality market board listings or vice versa",
                "description_localizations": {},
                "type": 5,
            },
        ],
    }

    command = translate_command(command)

    return command
