package dev.ag6.mclauncher.launch.tasks

import dev.ag6.mclauncher.launch.InstanceLauncher
import dev.ag6.mclauncher.minecraft.piston.PistonLibrary
import dev.ag6.mclauncher.task.DownloadTask

class DownloadLibraryTask(library: PistonLibrary) :
    DownloadTask(
        "Download Library: ${library.name}",
        library.download!!.url,
        InstanceLauncher.LIBRARY_LOCATION.resolve(library.getJarPath()),
        library.download.sha1
    ) {
}