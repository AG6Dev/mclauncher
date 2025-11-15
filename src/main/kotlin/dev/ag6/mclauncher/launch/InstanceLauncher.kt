package dev.ag6.mclauncher.launch

import dev.ag6.mclauncher.instance.GameInstance
import dev.ag6.mclauncher.launch.tasks.FetchAssetIndexTask
import dev.ag6.mclauncher.launch.tasks.FetchLibraryTask
import dev.ag6.mclauncher.launch.tasks.FetchMinecraftJarTask
import dev.ag6.mclauncher.launch.tasks.FetchVersionMetadataTask
import dev.ag6.mclauncher.task.CompositeTask
import dev.ag6.mclauncher.task.TaskExecutor
import dev.ag6.mclauncher.util.getDefaultDataLocation
import kotlinx.coroutines.runBlocking
import java.nio.file.Path

object InstanceLauncher {
    val METADATA_CACHE_LOCATION: Path = getDefaultDataLocation().resolve("version_meta")
    val LIBRARY_LOCATION: Path = getDefaultDataLocation().resolve("libraries")
    val CLIENTS_LOCATION: Path = getDefaultDataLocation().resolve("client")
    val ASSETS_LOCATION: Path = getDefaultDataLocation().resolve("assets")

    private val executor: TaskExecutor = TaskExecutor(8)
    private val launchProcesses: MutableMap<GameInstance, Process> = mutableMapOf()

    fun launchInstance(gameInstance: GameInstance) = runBlocking {
        val metadataTask = FetchVersionMetadataTask(gameInstance.version()!!)
        val metadata = executor.submit(metadataTask).await()

        val libTasks = metadata.libraries.map(::FetchLibraryTask)
        val libraryTask = CompositeTask("Download MC Libraries", true, libTasks)
        executor.submit(libraryTask).await()

        val clientJarTask = FetchMinecraftJarTask(metadata)
        val clientJar = executor.submit(clientJarTask).await()

        val assetTask = FetchAssetIndexTask(metadata)
        val assetIndex = executor.submit(assetTask).await()
    }

    fun prepareInstance(gameInstance: GameInstance) {

    }
}