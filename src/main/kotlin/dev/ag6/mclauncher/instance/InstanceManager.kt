package dev.ag6.mclauncher.instance

import com.google.gson.JsonObject
import dev.ag6.mclauncher.MCLauncher
import dev.ag6.mclauncher.minecraft.GameVersion
import dev.ag6.mclauncher.util.getDataLocation
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import java.nio.file.Files
import java.nio.file.Path

object InstanceManager {
    //make this configurable
    private val INSTANCE_DIRECTORY = getDataLocation().resolve("instances")
    val instances: ObservableList<GameInstance> = FXCollections.observableArrayList()

    fun loadAllInstances() {
        try {
            if (!Files.exists(INSTANCE_DIRECTORY)) {
                Files.createDirectories(INSTANCE_DIRECTORY)
                return
            }

           Files.list(INSTANCE_DIRECTORY).forEach {
               if(Files.isDirectory(it)) {
                   val result = loadInstanceFromDirectory(it)
                     if(result.isSuccess()) {
                          instances.add(result.instance)
                     } else {
                          MCLauncher.LOGGER.error { "Failed to load instance from ${it.fileName}: ${result.error}" }
                     }
               }
           }
        } catch (e: Exception) {
            MCLauncher.LOGGER.error(e) { "Failed to load instances" }
        }
    }

    fun saveAllInstances() {
        instances.forEach { instance ->
            try {
                val instanceDir = INSTANCE_DIRECTORY.resolve(instance.name.get())
                if (!Files.exists(instanceDir)) {
                    Files.createDirectories(instanceDir)
                }

                val instanceConfigFile = instanceDir.resolve("instance.json")
                Files.newBufferedWriter(instanceConfigFile).use { writer ->
                    val instanceJson = instance.toJson()
                    MCLauncher.GSON.toJson(instanceJson, writer)
                }
            } catch (e: Exception) {
                MCLauncher.LOGGER.error(e) { "Failed to save instance ${instance.id}" }
            }
        }
    }

    fun createInstance(name: String, version: GameVersion) {
        val newInstance = GameInstance(name, version)
        instances.add(newInstance)
    }

    private fun loadInstanceFromDirectory(path: Path): LoadInstanceResult {
        try {
            val instanceConfigFile = path.resolve("instance.json")

            if(!Files.exists(instanceConfigFile)) {
                return LoadInstanceResult.failure("Instance config file does not exist: $instanceConfigFile")
            }

            val instanceJson = Files.newBufferedReader(instanceConfigFile).use { reader ->
                MCLauncher.GSON.fromJson(reader, JsonObject::class.java)
            }

            val instance = GameInstance.fromJson(instanceJson)
            return LoadInstanceResult.success(instance)
        } catch (e: Exception) {
            return LoadInstanceResult.failure("Failed to load instance from directory $path: ${e.message}")
        }
    }


}
