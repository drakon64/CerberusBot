from lib.localize import translate_command

ephemeral = translate_command(
    {
        "name": "ephemeral",
        "name_localizations": {},
        "description": (
            "The message is only visible to the user who invoked the Interaction"
        ),
        "description_localizations": {},
        "type": 5,
    }
)
