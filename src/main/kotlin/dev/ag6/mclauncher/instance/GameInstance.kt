package dev.ag6.mclauncher.instance

import com.google.gson.JsonObject
import dev.ag6.mclauncher.minecraft.MinecraftVersion
import dev.ag6.mclauncher.minecraft.MinecraftVersionHandler
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import java.util.*

class GameInstance(id: UUID, name: String, version: MinecraftVersion?) {
    constructor(name: String, version: MinecraftVersion?) : this(UUID.randomUUID(), name, version)

    val name: SimpleStringProperty = SimpleStringProperty(name)

    val id: SimpleObjectProperty<UUID> = SimpleObjectProperty(id)

    val version: SimpleObjectProperty<MinecraftVersion> = SimpleObjectProperty(version)

    override fun toString(): String {
        return "GameInstance(id=$id, name='$name', version=$version)"
    }

    fun toJson(): JsonObject {
        val json = JsonObject()
        json.addProperty("id", id.get().toString())
        json.addProperty("name", name.get())
        json.addProperty("version", version.get().id)
        return json
    }

    companion object {
        fun fromJson(json: JsonObject): GameInstance {
            val id = UUID.fromString(json.get("id").asString)
            val name = json.get("name").asString
            val versionId = json.get("version").asString

            return GameInstance(id, name, MinecraftVersionHandler.getVersion(versionId))
        }
    }
}