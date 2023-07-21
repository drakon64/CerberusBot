from lib.localize import translate_command


async def create_lodestone_portrait_command():
    command = {
        "name": "Lodestone: Get character portrait",
        "name_localizations": {},
        "description": "Get a portrait of a users Final Fantasy XIV character",
        "description_localizations": {},
        "dm_permission": False,
        "type": 2,
    }

    return translate_command(command, False)
