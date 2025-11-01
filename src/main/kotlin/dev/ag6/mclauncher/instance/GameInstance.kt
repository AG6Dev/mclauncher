package dev.ag6.mclauncher.instance

import com.google.gson.JsonObject
import dev.ag6.mclauncher.minecraft.GameVersion
import dev.ag6.mclauncher.minecraft.GameVersionHandler
import dev.ag6.mclauncher.util.property.objectProperty
import dev.ag6.mclauncher.util.property.stringProperty
import java.util.*

class GameInstance(uuid: UUID, name: String, version: GameVersion?) {
    val name: String by stringProperty(name)

    val uuid: UUID by objectProperty(uuid)

    val version: GameVersion by objectProperty(version)

    override fun toString(): String {
        return "GameInstance(uuid=$uuid, name='$name', version=$version)"
    }

    fun toJson(): JsonObject {
        val json = JsonObject()
        json.addProperty("uuid", uuid.toString())
        json.addProperty("name", name)
        json.addProperty("version", version.id)
        return json
    }

    companion object {
        fun fromJson(json: JsonObject): GameInstance {
            val uuid = UUID.fromString(json.get("uuid").asString)
            val name = json.get("name").asString
            val versionId = json.get("version").asString

            return GameInstance(uuid, name, GameVersionHandler.getVersion(versionId))
        }
    }
}