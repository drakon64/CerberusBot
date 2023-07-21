from lib.localize import translate_command


async def create_lodestone_portrait_command():
    command = {
        "name": "Character portrait",
        "name_localizations": {},
        "dm_permission": False,
        "type": 2,
    }

    return translate_command(command, False)
