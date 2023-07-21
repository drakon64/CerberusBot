import json
import os

import boto3
from discord_interactions import InteractionResponseType, InteractionType, verify_key

lambda_client = boto3.client("lambda")
public_key = os.environ["PUBLIC_KEY"]

eorzea_database_function = os.environ["EORZEADATABASE_FUNCTION"]
lodestone_function = os.environ["LODESTONE_FUNCTION"]
universalis_function = os.environ["UNIVERSALIS_FUNCTION"]


def lambda_handler(event, context):
    headers = event["headers"]
    raw_body = event["body"].encode()

    if not verify_key(
        raw_body,
        headers["x-signature-ed25519"],
        headers["x-signature-timestamp"],
        public_key,
    ):
        return {"statusCode": 401}

    body = json.loads(raw_body)

    if body["type"] == InteractionType.PING:
        print("Received PING")

        return {"type": InteractionResponseType.PONG}
    elif body["type"] == InteractionType.APPLICATION_COMMAND:
        print("Deferring channel message")

        ephemeral = False

        if "options" in body["data"]:
            for option in body["data"]["options"]:
                if "options" in option:
                    for sub_option in option["options"]:
                        if sub_option["name"] == "ephemeral":
                            ephemeral = sub_option["value"]
                else:
                    if option["name"] == "ephemeral":
                        ephemeral = option["value"]

        match body["data"]["name"]:
            case "eorzea_database":
                function = eorzea_database_function
            case (
                "lodestone",
                "Character card",
                "Character portrait",
                "Character profile",
            ):
                function = lodestone_function
            case "universalis":
                function = universalis_function

        response = {
            "type": InteractionResponseType.DEFERRED_CHANNEL_MESSAGE_WITH_SOURCE
        }

        if ephemeral:
            response["data"] = {"flags": 1 << 6}

    else:
        raise Exception(f'Unknown interaction type :"f{body["type"]}"')

    lambda_client.invoke(
        FunctionName=function, InvocationType="Event", Payload=raw_body
    )

    return response
