package dev.ag6.mclauncher.instance

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import dev.ag6.mclauncher.minecraft.GameVersion
import dev.ag6.mclauncher.minecraft.GameVersionHandler
import dev.ag6.mclauncher.utils.property.objectProperty
import dev.ag6.mclauncher.utils.property.stringProperty
import java.util.*

class GameInstance(uuid: UUID, name: String, description: String, version: GameVersion?) {
    val name: String by stringProperty(name)

    val description: String by stringProperty(description)

    val uuid: UUID by objectProperty(uuid)

    val version: GameVersion by objectProperty(version)
}

object GameInstanceAdapter : TypeAdapter<GameInstance>() {
    override fun write(out: JsonWriter?, value: GameInstance?) {
        if (out == null || value == null) return

        out.beginObject()
        out.name("uuid").value(value.uuid.toString())
        out.name("name").value(value.name)
        out.name("description").value(value.description)
        out.name("version").value(value.version.id)
        out.endObject()
    }

    override fun read(reader: JsonReader?): GameInstance {
        if (reader == null) return GameInstance(UUID.randomUUID(), "Unknown", "No description", null)

        var uuid: UUID? = null
        var name: String? = null
        var description: String? = null
        var version: GameVersion? = null

        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "uuid" -> uuid = UUID.fromString(reader.nextString())
                "name" -> name = reader.nextString()
                "description" -> description = reader.nextString()
                "version" -> {
                    val id = reader.nextString()
                    version = GameVersionHandler.getVersionByString(id)
                }
            }
        }
        reader.endObject()

        return GameInstance(uuid ?: UUID.randomUUID(), name ?: "Unknown", description ?: "No description", version)
    }
}
