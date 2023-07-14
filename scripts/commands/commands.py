import json
import os

import requests

from eorzea_database import create_eorzea_database_command
from lodestone import create_lodestone_command
from universalis import create_universalis_command

application_id = os.environ["APPLICATION_ID"]
bot_token = os.environ["BOT_TOKEN"]

commands = (
    create_eorzea_database_command(),
    create_lodestone_command(),
    create_universalis_command(),
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
