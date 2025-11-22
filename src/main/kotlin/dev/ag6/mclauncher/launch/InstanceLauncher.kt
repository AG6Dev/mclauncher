package dev.ag6.mclauncher.launch

import dev.ag6.mclauncher.instance.GameInstance
import dev.ag6.mclauncher.launch.tasks.*
import dev.ag6.mclauncher.task.CompositeTask
import dev.ag6.mclauncher.task.TaskExecutor
import dev.ag6.mclauncher.util.getDefaultDataLocation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.nio.file.Path
import java.time.Instant

object InstanceLauncher {
    val METADATA_CACHE_LOCATION: Path = getDefaultDataLocation().resolve("version_meta")
    val LIBRARY_LOCATION: Path = getDefaultDataLocation().resolve("libraries")
    val CLIENTS_LOCATION: Path = getDefaultDataLocation().resolve("client")
    val ASSETS_LOCATION: Path = getDefaultDataLocation().resolve("assets")

    private val executor: TaskExecutor = TaskExecutor(8)
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val launchProcesses: MutableMap<GameInstance, Process> = mutableMapOf()

    fun launchInstance(gameInstance: GameInstance) = scope.launch {
        val metadataTask = FetchVersionMetadataTask(gameInstance.version()!!)
        val metadata = executor.submit(metadataTask).await()

        val libTasks = metadata.libraries.filter { it.isAllowedForSystem() }.map(::FetchLibraryTask)
        val libraryTask = CompositeTask("Download MC Libraries", true, libTasks)
        val libPaths: List<Path> = executor.submit(libraryTask).await() as List<Path>

        val clientJarTask = FetchMinecraftJarTask(metadata)
        val clientJar = executor.submit(clientJarTask).await()

        val assetTask = FetchAssetIndexTask(metadata)
        val assetIndex = executor.submit(assetTask).await()

        val assetTasks = assetIndex.assets.values.map(::DownloadAssetTask)
        val assetsDownloadTask = CompositeTask("Download MC Assets", true, assetTasks)
        executor.submit(assetsDownloadTask).await()

        val commandBuilder = LaunchProcessBuilder(gameInstance, metadata, ASSETS_LOCATION, clientJar, libPaths)
        val process = commandBuilder.build()

        launchProcesses[gameInstance] = process

        gameInstance.lastPlayed = Instant.now().toString()
        gameInstance.save()
    }
}