resource "aws_apigatewayv2_api" "defer" {
  name          = "DynamisBot_defer"
  protocol_type = "HTTP"

  target = aws_lambda_function.defer.invoke_arn
}

resource "aws_lambda_function" "defer" {
  function_name = "DynamisBot_defer"
  role          = aws_iam_role.lambda.arn

  environment {
    variables = {
      PUBLIC_KEY = var.discord_public_key
    }
  }

  filename    = "../defer/build/libs/defer-SNAPSHOT-1.0-all.jar"
  handler     = "cloud.drakon.dynamisbot.Handler"
  memory_size = 512
  publish     = true
  runtime     = "java21"

  snap_start {
    apply_on = "PublishedVersions"
  }

  timeout = 3
}

resource "aws_lambda_permission" "api_gateway" {
  action        = "lambda:InvokeFunction"
  function_name = aws_lambda_function.defer.function_name
  principal     = "apigateway.amazonaws.com"

  source_arn = "${aws_apigatewayv2_api.defer.execution_arn}/*/$default"
}

output "interactions_endpoint_url" {
  value = aws_apigatewayv2_api.defer.api_endpoint
}
