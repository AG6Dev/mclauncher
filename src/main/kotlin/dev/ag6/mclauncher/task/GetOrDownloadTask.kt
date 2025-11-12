package dev.ag6.mclauncher.task

import java.nio.file.Files
import java.nio.file.Path

class GetOrDownloadTask(
    override val name: String,
    url: String,
    private val downloadDestination: Path,
    private val sha1Hash: String = "",
    private val placesToCheck: List<Path>
) : DownloadTask(name, url, downloadDestination, sha1Hash) {
    override suspend fun execute(): Path {
        for (place in placesToCheck) {
            if (place == downloadDestination) continue
            if (Files.exists(place)) {
                if (sha1Hash.isNotEmpty()) {
                    val valid = checkHash(place, sha1Hash)
                    if (valid) {
                        return place
                    }
                } else {
                    return place
                }
            }
        }

        return super.execute()
    }
}