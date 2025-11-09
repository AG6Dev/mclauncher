package dev.ag6.mclauncher.launch.tasks

import dev.ag6.mclauncher.minecraft.piston.PistonLibrary
import dev.ag6.mclauncher.task.DownloadTask
import dev.ag6.mclauncher.util.getDefaultDataLocation
import java.nio.file.Path

class DownloadLibraryTask(library: PistonLibrary) :
    DownloadTask(
        "Download Library: ${library.name}",
        library.download!!.url,
        LIBRARY_LOCATION.resolve(library.getDirectory()),
        library.download.sha1
    ) {

    companion object {
        val LIBRARY_LOCATION: Path = getDefaultDataLocation().resolve("libraries")
    }
}