package cloud.drakon.tempestbot.interact.commands.citations

import cloud.drakon.ktdiscord.interaction.Interaction
import cloud.drakon.ktdiscord.interaction.applicationcommand.ApplicationCommandData
import cloud.drakon.tempestbot.interact.Handler.Companion.mongoDatabase
import com.amazonaws.services.lambda.runtime.LambdaLogger
import com.mongodb.client.MongoCollection
import org.bson.Document

val mongoCollection: MongoCollection<Document> =
    mongoDatabase.getCollection("citations")

suspend fun citationHandler(
    event: Interaction<ApplicationCommandData>,
    logger: LambdaLogger,
) {
    when (event.data !!.type) {
        1 -> when (event.data !!.options !![0].name) {
            "add" -> return addCitation(event, logger)
            "get" -> return getCitation(event)
            "opt-in" -> return optIn(event)
            "opt-out" -> return optOut(event)
        }

        2 -> return getCitation(event)

        3 -> return addCitation(event, logger)
    }
}
