package cloud.drakon.tempestbot.interact.commands.citations

import cloud.drakon.tempestbot.interact.mongoDatabase
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Projections
import org.bson.Document

val mongoCollection: MongoCollection<Document> =
    mongoDatabase.getCollection("citations")

class Citations(private val userId: String, private val guildId: String) {
    fun getCitation() {
        mongoCollection.find(
            Filters.and(
                eq("user_id", userId), eq("guild_id", guildId)
            )
        ).projection(
            Projections.fields(Projections.include("messages"), Projections.excludeId())
        ).first()
    }
}
