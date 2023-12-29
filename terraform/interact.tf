resource "aws_lambda_function" "interact" {
  function_name = "DynamisBot_interact"
  role          = aws_iam_role.lambda.arn

  environment {
    variables = {
      BOT_TOKEN = var.discord_bot_token
    }
  }

  filename    = var.defer_filename
  handler     = "cloud.drakon.dynamisbot.Handler"
  memory_size = 512
  publish     = true
  runtime     = "java21"

  snap_start {
    apply_on = "PublishedVersions"
  }

  source_code_hash = filebase64sha256(var.interact_filename)
  timeout          = 900
}
