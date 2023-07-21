from lib.localize import translate_command


async def create_lodestone_card_command():
    command = {
        "name": "Character card",
        "name_localizations": {},
        "description": "Get a character card of a users Final Fantasy XIV character",
        "description_localizations": {},
        "dm_permission": False,
        "type": 2,
    }

    return translate_command(command, False)
