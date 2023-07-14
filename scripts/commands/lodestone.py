from localize import translate_command


async def create_lodestone_command():
    command = {
        "name": "lodestone",
        "name_localizations": {},
        "description": "Final Fantasy XIV, The Lodestone",
        "description_localizations": {},
        "dm_permission": False,
        "type": 1,
        "options": [
            {
                "name": "card",
                "name_localizations": {},
                "description": (
                    "Get a character card of a users Final Fantasy XIV character"
                ),
                "description_localizations": {},
                "options": [
                    {
                        "name": "user",
                        "name_localizations": {},
                        "description": "The user to get a Lodestone card for",
                        "description_localizations": {},
                        "required": True,
                        "type": 6,
                    }
                ],
                "type": 1,
            },
            {
                "name": "link",
                "name_localizations": {},
                "description": (
                    "Link your Discord account to a Final Fantasy XIV character"
                ),
                "description_localizations": {},
                "type": 1,
                "options": [
                    {
                        "name": "character",
                        "name_localizations": {},
                        "description": (
                            "The name of the character to link your Discord account to"
                        ),
                        "description_localizations": {},
                        "required": True,
                        "type": 3,
                    },
                    {
                        "name": "world",
                        "name_localizations": {},
                        "description": (
                            "The world of the character to link your Discord account to"
                        ),
                        "description_localizations": {},
                        "required": True,
                        "type": 3,
                    },
                ],
            },
            {
                "name": "unlink",
                "name_localizations": {},
                "description": (
                    "Unlink your Discord account from a Final Fantasy XIV character"
                ),
                "description_localizations": {},
                "type": 1,
            },
            {
                "name": "portrait",
                "name_localizations": {},
                "description": "Get a portrait of a users Final Fantasy XIV character",
                "description_localizations": {},
                "type": 1,
                "options": [
                    {
                        "name": "user",
                        "name_localizations": {},
                        "description": "The user to get a Lodestone portrait for",
                        "description_localizations": {},
                        "required": True,
                        "type": 6,
                    }
                ],
            },
            {
                "name": "profile",
                "name_localizations": {},
                "description": "Get a profile of a users Final Fantasy XIV character",
                "description_localizations": {},
                "type": 1,
                "options": [
                    {
                        "name": "user",
                        "name_localizations": {},
                        "description": "The user to get a Lodestone profile for",
                        "description_localizations": {},
                        "required": True,
                        "type": 6,
                    }
                ],
            },
        ],
    }

    command = translate_command(command)

    return command
