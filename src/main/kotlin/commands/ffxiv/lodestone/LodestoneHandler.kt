package cloud.drakon.tempestbot.commands.ffxiv.lodestone

import cloud.drakon.ktdiscord.interaction.Interaction
import cloud.drakon.ktdiscord.interaction.applicationcommand.ApplicationCommandData

suspend fun lodestoneHandler(event: Interaction<ApplicationCommandData>) {
    when (event.data !!.options !![0].name) {
        "card" -> return card(event)
        "link" -> return link(event)
        "unlink" -> return unlink(event)
        "portrait" -> return portrait(event)
        "profile" -> return profile(event)
    }
}
