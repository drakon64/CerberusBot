import json
import os

import boto3
from discord_interactions import InteractionResponseType, InteractionType, verify_key

lambda_client = boto3.client("lambda")
public_key = os.environ["PUBLIC_KEY"]
interact_function = os.environ["INTERACT_FUNCTION"]


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
    elif body["type"] in (
        InteractionType.APPLICATION_COMMAND,
        InteractionType.APPLICATION_COMMAND_AUTOCOMPLETE,
    ):
        print("Deferring channel message")
        response = {
            "type": InteractionResponseType.DEFERRED_CHANNEL_MESSAGE_WITH_SOURCE
        }
    elif body["type"] in (
        InteractionType.MESSAGE_COMPONENT,
        InteractionType.MODAL_SUBMIT,
    ):
        print("Deferring update message")
        response = {"type": InteractionResponseType.DEFERRED_UPDATE_MESSAGE}
    else:
        raise Exception(f'Unknown interaction type :"f{body["type"]}"')

    lambda_client.invoke(
        FunctionName=interact_function, InvocationType="Event", Payload=raw_body
    )

    return response
