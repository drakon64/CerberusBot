package cloud.drakon.tempestbot.interact.commands.ffxiv.lodestone

import cloud.drakon.tempest.interaction.Interaction
import cloud.drakon.tempest.interaction.applicationcommand.ApplicationCommandData

suspend fun lodestoneHandler(event: Interaction<ApplicationCommandData>) {
    when (event.data !!.options !![0].name) {
        "link" -> return link(event)
        "unlink" -> return unlink(event)
    }
}
