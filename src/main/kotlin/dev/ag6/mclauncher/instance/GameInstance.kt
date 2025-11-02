package dev.ag6.mclauncher.instance

import com.google.gson.JsonObject
import dev.ag6.mclauncher.minecraft.GameVersion
import dev.ag6.mclauncher.minecraft.GameVersionHandler
import dev.ag6.mclauncher.util.property.objectProperty
import dev.ag6.mclauncher.util.property.stringProperty
import java.util.*

class GameInstance(id: UUID, name: String, version: GameVersion?) {
    constructor(name: String, version: GameVersion?) : this(UUID.randomUUID(), name, version)
    val name: String by stringProperty(name)

    val id: UUID by objectProperty(id)

    val version: GameVersion by objectProperty(version)

    override fun toString(): String {
        return "GameInstance(id=$id, name='$name', version=$version)"
    }

    fun toJson(): JsonObject {
        val json = JsonObject()
        json.addProperty("id", id.toString())
        json.addProperty("name", name)
        json.addProperty("version", version.id)
        return json
    }

    companion object {
        fun fromJson(json: JsonObject): GameInstance {
            val id = UUID.fromString(json.get("id").asString)
            val name = json.get("name").asString
            val versionId = json.get("version").asString

            return GameInstance(id, name, GameVersionHandler.getVersion(versionId))
        }
    }
}