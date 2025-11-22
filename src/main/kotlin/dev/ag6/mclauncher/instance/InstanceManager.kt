package dev.ag6.mclauncher.instance

import dev.ag6.mclauncher.MCLauncher
import dev.ag6.mclauncher.minecraft.MinecraftVersion
import dev.ag6.mclauncher.util.getDefaultDataLocation
import dev.ag6.mclauncher.util.toPath
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import java.nio.file.Files

object InstanceManager {
    private val INSTANCE_DIRECTORY = getDefaultDataLocation().resolve("instances")
    val instances: ObservableList<GameInstance> = FXCollections.observableArrayList()

    fun loadAllInstances() {
        try {
            if (!Files.exists(INSTANCE_DIRECTORY)) {
                Files.createDirectories(INSTANCE_DIRECTORY)
                return
            }

            Files.list(INSTANCE_DIRECTORY).forEach {
                if (Files.isDirectory(it)) {
                    val result = GameInstance.fromDirectory(it)
                    if (result.isSuccess()) {
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

    fun createInstance(name: String, version: MinecraftVersion): GameInstance {
        val newInstance =
            GameInstance(name = name, version = { version }, directory = INSTANCE_DIRECTORY.resolve(name).toString())
        newInstance.createDirectories()
        newInstance.save()

        instances.add(newInstance)
        return newInstance
    }

    fun saveAllInstances() {
        instances.forEach(GameInstance::save)
    }

    fun deleteInstance(instance: GameInstance) {
        try {
            val instanceDir = instance.directory
            if (Files.exists(instanceDir.toPath())) {
                Files.walk(instanceDir.toPath())
                    .sorted(Comparator.reverseOrder())
                    .forEach(Files::delete)
            }
            instances.remove(instance)
        } catch (e: Exception) {
            instance.save()
            MCLauncher.LOGGER.error(e) { "Failed to delete instance ${instance.id}, undoing" }
        }
    }
}
