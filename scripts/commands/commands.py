import asyncio
import json
import os

import requests

from commands.eorzea_database import create_eorzea_database_command
from commands.lodestone import create_lodestone_command
from commands.universalis import create_universalis_command
from commands.user.lodestone.card import create_lodestone_card_command
from commands.user.lodestone.portrait import create_lodestone_portrait_command
from commands.user.lodestone.profile import create_lodestone_profile_command


async def main():
    eorzea_database = asyncio.create_task(create_eorzea_database_command())
    lodestone = asyncio.create_task(create_lodestone_command())
    universalis = asyncio.create_task(create_universalis_command())

    lodestone_card = asyncio.create_task(create_lodestone_card_command())
    lodestone_portrait = asyncio.create_task(create_lodestone_portrait_command())
    lodestone_profile = asyncio.create_task(create_lodestone_profile_command())

    if "APPLICATION_ID" in os.environ and "BOT_TOKEN" in os.environ:
        put_commands = True

        application_id = os.environ["APPLICATION_ID"]
        bot_token = os.environ["BOT_TOKEN"]
    else:
        put_commands = False

    commands = (
        await eorzea_database,
        await lodestone,
        await lodestone_card,
        await lodestone_portrait,
        await lodestone_profile,
        await universalis,
    )

    json.dump(commands, open("commands.json", "w"), ensure_ascii=False, indent=4)

    if put_commands:
        request = requests.put(
            f"https://discord.com/api/v10/applications/{application_id}/commands",
            headers={
                "Authorization": f"Bot {bot_token}",
            },
            json=commands,
        )

        print(
            json.dumps(
                json.loads(bytes.decode(request.content)),
                indent=4,
            )
        )

        if request.status_code != 200:
            raise Exception()


asyncio.run(main())
