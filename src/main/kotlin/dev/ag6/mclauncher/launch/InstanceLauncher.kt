package dev.ag6.mclauncher.launch

import dev.ag6.mclauncher.instance.GameInstance
import dev.ag6.mclauncher.launch.tasks.DownloadLibraryTask
import dev.ag6.mclauncher.launch.tasks.GetVersionMetadataTask
import dev.ag6.mclauncher.minecraft.piston.PistonLibrary
import dev.ag6.mclauncher.minecraft.piston.PistonVersionMetadata
import dev.ag6.mclauncher.task.CompositeTask
import dev.ag6.mclauncher.task.TaskExecutor
import dev.ag6.mclauncher.util.getDefaultDataLocation
import kotlinx.coroutines.runBlocking
import java.nio.file.Files
import java.nio.file.Path

object InstanceLauncher {
    val METADATA_CACHE_LOCATION: Path = getDefaultDataLocation().resolve("version_meta")
    private val executor: TaskExecutor = TaskExecutor(8)
    private val launchProcesses: MutableMap<GameInstance, Process> = mutableMapOf()

    fun launchInstance(gameInstance: GameInstance) = runBlocking {
        val metadataTask = GetVersionMetadataTask(gameInstance.version()!!)
        val metadata = executor.submit(metadataTask).await()

        val libTasks: List<DownloadLibraryTask> = getLibrariesToDownload(metadata).map(::DownloadLibraryTask)
        val libraryTask = CompositeTask("Download MC Libraries", true, libTasks)
        executor.submit(libraryTask).await()
    }

    fun prepareInstance(gameInstance: GameInstance) {

    }

    private fun getLibrariesToDownload(meta: PistonVersionMetadata): List<PistonLibrary> {
        return meta.libraries.filter {
            var allowed = false
            try {
                if (it.download == null) return@filter false

                val rules = it.rules ?: return@filter true

                val os = System.getProperty("os.name").lowercase()
                val osName = when {
                    os.contains("win") -> "windows"
                    os.contains("mac") -> "osx"
                    else -> "linux"
                }


                for (rule in rules) {
                    val osRule = rule.os
                    if (osRule.name == osName) {
                        allowed = rule.action == PistonLibrary.LibraryRule.Action.ALLOW
                    }
                }

                allowed && !Files.exists(DownloadLibraryTask.LIBRARY_LOCATION.resolve(it.name))
            } catch (e: Exception) {
                allowed
            }

        }
    }
}