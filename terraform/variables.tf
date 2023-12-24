variable "discord_public_key" {
  type = string
  sensitive = true
}

variable "defer_filename" {
  type = string
  default = "../defer/build/libs/defer-SNAPSHOT-1.0-all.jar"
}
