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
      DISCORD_PUBLIC_KEY = var.discord_public_key
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

  source_code_hash = filebase64sha256(var.defer_filename)
  timeout          = 3
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
