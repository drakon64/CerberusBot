package cloud.drakon.dynamisbot

internal fun String.commandName() = this.lowercase()
    .replace(" ", "_")
    .replace("'", "_")
