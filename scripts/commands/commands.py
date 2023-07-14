import asyncio
import json
import os

import requests

from eorzea_database import create_eorzea_database_command
from lodestone import create_lodestone_command
from universalis import create_universalis_command


async def main():
    application_id = os.environ["APPLICATION_ID"]
    bot_token = os.environ["BOT_TOKEN"]

    eorzea_database = asyncio.create_task(create_eorzea_database_command())
    lodestone = asyncio.create_task(create_lodestone_command())
    universalis = asyncio.create_task(create_universalis_command())

    commands = (
        await eorzea_database,
        await lodestone,
        await universalis,
    )

    json.dump(commands, open("commands.json", "w"), ensure_ascii=False, indent=4)

    print(
        json.dumps(
            json.loads(
                bytes.decode(
                    requests.put(
                        f"https://discord.com/api/v10/applications/{application_id}/commands",
                        headers={
                            "Authorization": f"Bot {bot_token}",
                            "Content-Type": "application/json",
                        },
                        data=json.dumps(commands),
                    ).content
                )
            ),
            indent=4,
        )
    )


asyncio.run(main())
