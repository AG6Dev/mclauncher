package dev.ag6.mclauncher.instance

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import kotlinx.coroutines.*
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

class InstanceManager(private val instancesPath: String) {
    val instances: ObservableList<GameInstance> = FXCollections.observableArrayList()

    init {
        Files.createDirectories(Paths.get(instancesPath))

        for (i in 0..4) {
            createInstance()
        }
    }

    fun createInstance() {
        val newInstance = GameInstance(UUID.randomUUID(), "New Instance", "This is a new instance.")
        instances.add(newInstance)
    }

    private suspend fun saveInstanceToDisk(gameInstance: GameInstance): GameInstance = withContext(Dispatchers.IO) {
        try {
            val filePath = "$instancesPath/${gameInstance.uuid}/instance.json"

            val jsonString = gson.toJson(gameInstance)
            Files.createDirectories(Paths.get(filePath).parent)
            Files.writeString(Paths.get(filePath), jsonString)

            gameInstance
        } catch (e: Exception) {
            error("Failed to save instance ${gameInstance.uuid} to disk: ${e.message}")
        }
    }

    suspend fun saveAllInstances(): List<GameInstance> = coroutineScope {
        println("Saving all instances to disk...")
        val deferredSaves = instances.map {
            async {
                saveInstanceToDisk(it)
            }
        }
        val savedInstances = deferredSaves.awaitAll()
        println("Saved ${savedInstances.size} instances to disk.")
        savedInstances
    }

    companion object {
        private val gson: Gson = GsonBuilder().setPrettyPrinting()
            .registerTypeAdapter(GameInstance::class.java, GameInstanceAdapter)
            .disableHtmlEscaping()
            .create()
    }
}
