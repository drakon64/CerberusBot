package cloud.drakon.dynamisbot.universalis

import cloud.drakon.ktdiscord.KtDiscord
import cloud.drakon.ktuniversalis.KtUniversalis
import cloud.drakon.ktxivapi.KtXivApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise
import kotlinx.serialization.json.Json

// Initialise these during the initialization phase

val json = Json {
    ignoreUnknownKeys =
        true // Not all fields returned by the Discord API are documented
    isLenient = true // TODO https://github.com/TempestProject/Tempest/issues/3
}

@JsName("process")
external object Process {
    val env: dynamic
}

val ktDiscord = KtDiscord(
    Process.env.APPLICATION_ID as String,
    Process.env.BOT_TOKEN as String,
).Interaction(Process.env.PUBLIC_KEY as String)

val ktXivApi = KtXivApi
val ktUniversalis = KtUniversalis

val spanRegex = """<span.*?>|</span>""".toRegex()
val newLineRegex = """\n{3,}""".toRegex()

@JsExport
fun handler(event: JSON, context: Any?) = GlobalScope.promise {
    universalisCommand(json.decodeFromString(JSON.stringify(event)))
}
