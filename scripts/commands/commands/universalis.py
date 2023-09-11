from lib.localize import translate_command
from option.ephemeral import ephemeral


async def create_universalis_command():
    command = {
        "name": "universalis",
        "name_localizations": {},
        "description": "Get Final Fantasy XIV market board listings",
        "description_localizations": {},
        "type": 1,
        "skip_localization": True,
        "options": (
            {
                "name": "world",
                "name_localizations": {},
                "description": (
                    "Get Final Fantasy XIV market board listings for a world"
                ),
                "description_localizations": {},
                "options": (
                    {
                        "name": "world",
                        "name_localizations": {},
                        "description": "The world to search in",
                        "description_localizations": {},
                        "required": True,
                        "type": 3,
                    },
                    {
                        "name": "item",
                        "name_localizations": {},
                        "description": "The item to search for",
                        "description_localizations": {},
                        "required": True,
                        "type": 3,
                    },
                    {
                        "name": "high quality",
                        "name_localizations": {},
                        "description": (
                            "Only show high quality market board listings or vice versa"
                        ),
                        "description_localizations": {},
                        "type": 5,
                    },
                    ephemeral,
                ),
                "type": 1,
            },
            {
                "name": "datacenter",
                "name_localizations": {},
                "description": (
                    "Get Final Fantasy XIV market board listings for a datacenter"
                ),
                "description_localizations": {},
                "options": (
                    {
                        "name": "datacenter",
                        "name_localizations": {},
                        "description": "The datacenter to search in",
                        "description_localizations": {},
                        "required": True,
                        "type": 3,
                        "choices": (
                            {"name": "Elemental", "value": "Elemental"},
                            {"name": "Gaia", "value": "Gaia"},
                            {"name": "Mana", "value": "Mana"},
                            {"name": "Aether", "value": "Aether"},
                            {"name": "Primal", "value": "Primal"},
                            {"name": "Chaos", "value": "Chaos"},
                            {"name": "Light", "value": "Light"},
                            {"name": "Crystal", "value": "Crystal"},
                            {"name": "Materia", "value": "Materia"},
                            {"name": "Meteor", "value": "Meteor"},
                            {"name": "Dynamis", "value": "Dynamis"},
                        ),
                    },
                    {
                        "name": "item",
                        "name_localizations": {},
                        "description": "The item to search for",
                        "description_localizations": {},
                        "required": True,
                        "type": 3,
                    },
                    {
                        "name": "high quality",
                        "name_localizations": {},
                        "description": (
                            "Only show high quality market board listings or vice versa"
                        ),
                        "description_localizations": {},
                        "type": 5,
                    },
                    ephemeral,
                ),
                "type": 1,
            },
            {
                "name": "region",
                "name_localizations": {},
                "description": (
                    "Get Final Fantasy XIV market board listings for a region"
                ),
                "description_localizations": {},
                "options": (
                    {
                        "name": "region",
                        "name_localizations": {},
                        "description": "The region to search in",
                        "description_localizations": {},
                        "required": True,
                        "type": 3,
                        "choices": (
                            {
                                "name": "Japan",
                                "value": "Japan",
                                "name_localizations": {},
                            },
                            {
                                "name": "North America",
                                "value": "NorthAmerica",
                                "name_localizations": {},
                            },
                            {
                                "name": "Europe",
                                "value": "Europe",
                                "name_localizations": {},
                            },
                            {
                                "name": "Oceania",
                                "value": "Oceania",
                                "name_localizations": {},
                            },
                        ),
                    },
                    {
                        "name": "item",
                        "name_localizations": {},
                        "description": "The item to search for",
                        "description_localizations": {},
                        "required": True,
                        "type": 3,
                    },
                    {
                        "name": "high quality",
                        "name_localizations": {},
                        "description": (
                            "Only show high quality market board listings or vice versa"
                        ),
                        "description_localizations": {},
                        "type": 5,
                    },
                    ephemeral,
                ),
                "type": 1,
            },
        ),
    }

    return translate_command(command)
