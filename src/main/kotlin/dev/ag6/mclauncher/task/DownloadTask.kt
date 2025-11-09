package dev.ag6.mclauncher.task

import dev.ag6.mclauncher.MCLauncher
import dev.ag6.mclauncher.minecraft.piston.Download
import javafx.application.Platform
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Request
import java.nio.file.Files
import java.nio.file.Path
import java.security.MessageDigest
import kotlin.io.path.outputStream

open class DownloadTask(
    override val name: String,
    private val url: String,
    private val destination: Path,
    private val sha1Hash: String? = null
) : Task<Path> {
    constructor(name: String, destination: Path, download: Download) : this(
        name,
        download.url,
        destination,
        download.sha1
    )

    override suspend fun execute(): Path = withContext(Dispatchers.IO) {
        if (isCancelled()) throw CancellationException("Task cancelled")

        Platform.runLater { stateProperty.set(Task.State.RUNNING) }

        Files.createDirectories(destination.parent)

        val request = Request.Builder().get().url(url).build()
        MCLauncher.HTTP_CLIENT.newCall(request).execute().use {
            if (!it.isSuccessful) {
                throw IllegalStateException("Failed to download file from $url: ${it.code}")
            }

            val body = it.body
            val size = body.contentLength()

            body.byteStream().use { stream ->
                {
                    destination.outputStream().use { output ->
                        val buffer = ByteArray(8192)
                        var bytesRead: Int
                        var totalBytesRead: Long = 0

                        while (stream.read(buffer).also { bytesRead = it } != -1) {
                            if (isCancelled()) throw CancellationException("Task cancelled")

                            output.write(buffer, 0, bytesRead)
                            totalBytesRead += bytesRead

                            val progress = if (size > 0) {
                                bytesRead.toFloat() / size
                            } else {
                                0.0f
                            }

                            setProgress(progress)
                        }
                    }
                }
            }

        }

        sha1Hash?.let {
            if (!checkHash(destination, it)) {
                throw IllegalStateException("SHA-1 hash mismatch for downloaded file from $url")
            }
        }

        setState(Task.State.COMPLETED)
        setProgress(1.0f)

        destination
    }

    private fun checkHash(file: Path, expectedHash: String): Boolean {
        val digest = MessageDigest.getInstance("SHA-1")
        Files.newInputStream(file).use { inputStream ->
            val buffer = ByteArray(8192)
            var bytesRead: Int
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                digest.update(buffer, 0, bytesRead)
            }
        }

        val fileHash = digest.digest().joinToString("") { "%02x".format(it) }
        return fileHash.equals(expectedHash, ignoreCase = true)
    }

}