package cloud.drakon.tempestbot.interact.commands.citations

import cloud.drakon.tempest.interaction.Interaction
import cloud.drakon.tempest.interaction.applicationcommand.ApplicationCommandData
import cloud.drakon.tempestbot.interact.Handler.Companion.mongoDatabase
import com.mongodb.client.MongoCollection
import org.bson.Document

val mongoCollection: MongoCollection<Document> =
    mongoDatabase.getCollection("citations")

suspend fun citationHandler(event: Interaction<ApplicationCommandData>) {
    lateinit var userId: String
    val guildId: String = event.guild_id !!

    when (event.data !!.type) {
        1 -> {
            val options = event.data !!.options !![0]

            for (i in options.options !!) {
                if (i.name == "user") {
                    userId = i.value !!
                }
            }

            when (options.name) {
                "add" -> return addCitation(event)
                "get" -> return getCitation(event, userId, guildId)
                "opt-in" -> return optIn(event, guildId)
                "opt-out" -> return optOut(event, guildId)
            }
        }

        2 -> {
            userId = event.data !!.resolved !!.users !!.keys.first()

            return getCitation(event, userId, guildId)
        }

        3 -> return addCitation(event)
    }
}
