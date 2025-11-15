package dev.ag6.mclauncher.launch.tasks

import dev.ag6.mclauncher.MCLauncher
import dev.ag6.mclauncher.launch.InstanceLauncher
import dev.ag6.mclauncher.minecraft.piston.PistonLibrary
import dev.ag6.mclauncher.task.DownloadTask
import dev.ag6.mclauncher.task.Task
import dev.ag6.mclauncher.util.verifyFileSHA1
import java.nio.file.Path
import kotlin.io.path.deleteIfExists
import kotlin.io.path.exists

class FetchLibraryTask(private val library: PistonLibrary) : Task<Path> {
    override val name: String = "Fetch Library: ${library.name}"

    override suspend fun execute(): Path {
        val libPath = InstanceLauncher.LIBRARY_LOCATION.resolve(library.getJarPath())

        if (libPath.exists()) {
            if (verifyFileSHA1(libPath, library.download!!.sha1)) {
                return libPath
            }

            libPath.deleteIfExists()
            MCLauncher.LOGGER.warn { "Existing library ${library.name} has invalid hash, re-downloading." }
        }

        if (library.download == null) throw IllegalArgumentException("Library ${library.name} does not have a download URL.")

        val downloadTask = DownloadTask(name, library.download.url, libPath, library.download.sha1)
        downloadTask.execute()

        return libPath
    }
}