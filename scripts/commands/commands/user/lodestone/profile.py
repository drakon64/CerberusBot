from lib.localize import translate_command


async def create_lodestone_profile_command():
    command = {
        "name": "Lodestone: Get character profile",
        "name_localizations": {},
        "description": "Generate an embed of a users Final Fantasy XIV character",
        "description_localizations": {},
        "dm_permission": False,
        "type": 2,
    }

    return translate_command(command, False)
