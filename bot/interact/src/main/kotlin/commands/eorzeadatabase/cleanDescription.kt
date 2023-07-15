package cloud.drakon.dynamisbot.interact.commands.eorzeadatabase

import kotlinx.coroutines.coroutineScope
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.safety.Safelist

suspend fun cleanDescription(description: String) = coroutineScope {
    return@coroutineScope Jsoup.clean(
        description, "", Safelist.none(), Document.OutputSettings().prettyPrint(false)
    ).replace("""\n{3,}""".toRegex(), "\n\n")
}
