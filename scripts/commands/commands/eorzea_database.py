from lib.localize import translate_command
from option.ephemeral import ephemeral


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
                    {"name": "Quest", "name_localizations": {}, "value": "quest"},
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
            ephemeral,
        ),
    }

    return translate_command(command)
