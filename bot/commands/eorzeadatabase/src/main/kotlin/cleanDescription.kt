package cloud.drakon.dynamisbot.eorzeadatabase

import cloud.drakon.dynamisbot.eorzeadatabase.Handler.Companion.newLineRegex
import cloud.drakon.dynamisbot.eorzeadatabase.Handler.Companion.spanRegex
import kotlinx.coroutines.coroutineScope

suspend fun cleanDescription(description: String) = coroutineScope {
    return@coroutineScope description
        .replace(spanRegex, "")
        .replace(newLineRegex, "\n\n")
}
