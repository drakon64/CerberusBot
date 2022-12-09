package cloud.drakon.tempestbot.interact

import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase

val mongoDatabase: MongoDatabase = MongoClients.create("").getDatabase("TempestBot")
