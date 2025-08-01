package dev.ag6.mclauncher.instance

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dev.ag6.mclauncher.minecraft.GameVersion
import dev.ag6.mclauncher.minecraft.GameVersionHandler
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

class InstanceManager(dataDirectory: Path) {
    val instances: ObservableList<GameInstance> = FXCollections.observableArrayList()
    private val instancesPath: Path = dataDirectory.resolve("instances")

    init {
        Files.createDirectories(instancesPath)
    }

    fun createInstance(name: String, description: String, version: GameVersion) {
        val newInstance = GameInstance(UUID.randomUUID(), name, description, version)
        GameVersionHandler.fetchVersionMeta(version)
        instances.add(newInstance)
    }

    //TODO: coroutines
    fun loadInstances() {
        try {
            val files = Files.list(instancesPath)
                .filter(Files::isDirectory)
                .toList()

            instances.clear()
            for (file in files) {
                val instanceFile = file.resolve("instance.json")
                if (Files.exists(instanceFile)) {
                    val jsonString = Files.readString(instanceFile)
                    val gameInstance: GameInstance = gson.fromJson(jsonString, GameInstance::class.java)
                    instances.add(gameInstance)
                } else {
                    println("Warning: Instance file not found for ${file.fileName}")
                }
            }
            println("Loaded ${instances.size} instances")
        } catch (e: Exception) {
            error("Failed to load instances from disk: ${e.message}")
        }
    }

    private fun saveInstanceToDisk(gameInstance: GameInstance) {
        try {
            val filePath = instancesPath.resolve("${gameInstance.uuid}/instance.json")

            val jsonString = gson.toJson(gameInstance)
            Files.createDirectories(filePath.parent)
            Files.writeString(filePath, jsonString)
        } catch (e: Exception) {
            error("Failed to save instance ${gameInstance.uuid} to disk: ${e.message}")
        }
    }

    fun saveAllInstances() {
        println("Saving all instances to disk...")

        instances.forEach { saveInstanceToDisk(it) }

        println("Saved ${instances.size} instances to disk.")
    }

    companion object {
        private val gson: Gson = GsonBuilder().setPrettyPrinting()
            .registerTypeAdapter(GameInstance::class.java, GameInstanceAdapter)
            .disableHtmlEscaping()
            .create()
    }
}
