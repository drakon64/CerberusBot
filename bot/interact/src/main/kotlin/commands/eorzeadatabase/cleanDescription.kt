package cloud.drakon.dynamisbot.interact.commands.eorzeadatabase

import cloud.drakon.dynamisbot.interact.Handler.Companion.newLineRegex
import cloud.drakon.dynamisbot.interact.Handler.Companion.spanRegex
import kotlinx.coroutines.coroutineScope

suspend fun cleanDescription(description: String) = coroutineScope {
    return@coroutineScope description
        .replace(spanRegex, "")
        .replace(newLineRegex, "\n\n")
}
