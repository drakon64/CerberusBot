from localize import translate_command


async def create_eorzea_database_command():
    command = {
        "name": "Eorzea Database",
        "name_localizations": {},
        "description": "Search the Eorzea Database",
        "description_localizations": {},
        "type": 1,
        "options": (
            {
                "name": "index",
                "name_localizations": {},
                "description": "The index to search in",
                "description_localizations": {},
                "required": True,
                "choices": (
                    {"name": "Item", "name_localizations": {}, "value": "item"},
                ),
                "type": 3,
            },
            {
                "name": "query",
                "name_localizations": {},
                "description": "The search query",
                "description_localizations": {},
                "required": True,
                "type": 3,
            },
            {
                "name": "language",
                "name_localizations": {},
                "description": "The language to search in",
                "description_localizations": {},
                "choices": (
                    {"name": "English", "name_localizations": {}, "value": "en"},
                    {"name": "Japanese", "name_localizations": {}, "value": "ja"},
                    {"name": "German", "name_localizations": {}, "value": "de"},
                    {"name": "French", "name_localizations": {}, "value": "fr"},
                ),
                "type": 3,
            },
            {
                "name": "ephemeral",
                "name_localizations": {},
                "description": (
                    "The message is only visible to the user who invoked the"
                    " Interaction"
                ),
                "description_localizations": {},
                "type": 5,
            },
        ),
    }

    command = translate_command(command)

    return command
