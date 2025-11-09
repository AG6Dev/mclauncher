package dev.ag6.mclauncher.instance

import com.google.gson.JsonObject
import dev.ag6.mclauncher.MCLauncher
import dev.ag6.mclauncher.minecraft.MinecraftVersion
import dev.ag6.mclauncher.minecraft.MinecraftVersionHandler
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

data class GameInstance(
    val id: UUID = UUID.randomUUID(),
    var name: String,
    var version: () -> MinecraftVersion?,
    var javaPath: Path? = null,
    var directory: Path,
    var lastPlayed: String? = null,
    var additionalArgs: List<String> = emptyList(),
    var memoryAllocation: Int? = 2048
) {

    fun save() {
        try {
            Files.createDirectories(directory)
            val json = this.toJson()

            val instanceConfigFile = directory.resolve("instance.json")
            Files.newBufferedWriter(instanceConfigFile).use { writer ->
                MCLauncher.GSON.toJson(json, writer)
            }
        } catch (e: Exception) {
            MCLauncher.LOGGER.error(e) { "Error while saving instance $name, $id" }
        }
    }

    private fun toJson(): JsonObject {
        val json = JsonObject()
        json.addProperty("id", id.toString())
        json.addProperty("name", name)
        json.addProperty("version", version()?.id)
        javaPath?.let { json.addProperty("javaPath", it.toString()) }
        json.addProperty("directory", directory.toString())
        lastPlayed?.let { json.addProperty("lastPlayed", it) }
        if (additionalArgs.isNotEmpty()) {
            val argsArray = com.google.gson.JsonArray()
            additionalArgs.forEach { argsArray.add(it) }
            json.add("additionalArgs", argsArray)
        }
        memoryAllocation?.let { json.addProperty("memoryAllocation", it) }
        return json
    }

    override fun toString(): String {
        return "GameInstance(id=$id, name='$name', version=${version()?.id}, javaPath=$javaPath, directory=$directory, lastPlayed=$lastPlayed, additionalArgs=$additionalArgs, memoryAllocation=$memoryAllocation)"
    }

    companion object {
        fun fromJson(json: JsonObject): GameInstance {
            val id = UUID.fromString(json.get("id").asString)
            val name = json.get("name").asString
            val versionId = json.get("version").asString
            val versionProvider = {
                MinecraftVersionHandler.getVersion(versionId)
            }
            val javaPath = if (json.has("javaPath")) Path.of(json.get("javaPath").asString) else null
            val directory = Path.of(json.get("directory").asString)
            val lastPlayed = if (json.has("lastPlayed")) json.get("lastPlayed").asString else null
            val additionalArgs = if (json.has("additionalArgs")) {
                json.getAsJsonArray("additionalArgs").map { it.asString }
            } else {
                emptyList()
            }
            val memoryAllocation = if (json.has("memoryAllocation")) json.get("memoryAllocation").asInt else null

            return GameInstance(
                id = id,
                name = name,
                version = versionProvider,
                javaPath = javaPath,
                directory = directory,
                lastPlayed = lastPlayed,
                additionalArgs = additionalArgs,
                memoryAllocation = memoryAllocation
            )
        }

        fun fromDirectory(path: Path): LoadInstanceResult {
            try {
                val configFile = path.resolve("instance.json")
                if (!Files.exists(configFile)) {
                    return LoadInstanceResult.failure("Instance config file does not exist: $configFile")
                }

                val instanceJson = Files.newBufferedReader(configFile).use { reader ->
                    MCLauncher.GSON.fromJson(reader, JsonObject::class.java)
                }
                val instance = fromJson(instanceJson)
                return LoadInstanceResult.success(instance)
            } catch (e: Exception) {
                return LoadInstanceResult.failure("Failed to load instance from directory $path: ${e.message}")
            }
        }
    }
}