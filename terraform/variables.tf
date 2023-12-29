variable "aws_access_key" {
  type = string
  sensitive = true
}

variable "aws_secret_access_key" {
  type = string
  sensitive = true
}

variable "discord_public_key" {
  type = string
}

variable "defer_filename" {
  type = string
  default = "../defer/build/libs/defer-SNAPSHOT-1.0-all.jar"
}

variable "discord_bot_token" {
  type = string
  sensitive = true
}

variable "interact_filename" {
  type = string
  default = "../interact/build/libs/interact-SNAPSHOT-1.0-all.jar"
}
