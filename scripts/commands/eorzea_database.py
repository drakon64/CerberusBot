from localize import translate_command


def create_eorzea_database_command():
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
                "type": 3,
                "choices": (
                    {"name": "Item", "name_localizations": {}, "value": "item"},
                ),
            },
            {
                "name": "query",
                "name_localizations": {},
                "description": "The search query",
                "description_localizations": {},
                "type": 3,
            },
            {
                "name": "language",
                "name_localizations": {},
                "description": "The language to search in",
                "description_localizations": {},
                "type": 3,
                "required": False,
                "choices": (
                    {"name": "English", "name_localizations": {}, "value": "en"},
                    {"name": "Japanese", "name_localizations": {}, "value": "ja"},
                    {"name": "German", "name_localizations": {}, "value": "de"},
                    {"name": "French", "name_localizations": {}, "value": "fr"},
                ),
            },
        ),
    }

    command = translate_command(command)

    return command
