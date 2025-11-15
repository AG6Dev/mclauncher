package dev.ag6.mclauncher.task

import dev.ag6.mclauncher.MCLauncher
import dev.ag6.mclauncher.util.verifyFileSHA1
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Request
import java.nio.file.Path
import kotlin.io.path.createParentDirectories
import kotlin.io.path.deleteIfExists
import kotlin.io.path.outputStream

open class DownloadTask(
    override val name: String,
    private val url: String,
    private val destination: Path,
    private val sha1Hash: String? = null
) : Task<Path> {
    override suspend fun execute(): Path = withContext(Dispatchers.IO) {
        if (isCancelled()) throw CancellationException("Task cancelled")

        setState(Task.State.RUNNING)

        destination.createParentDirectories()

        val request = Request.Builder().get().url(url).build()
        MCLauncher.HTTP_CLIENT.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                throw IllegalStateException("Failed to download file from $url: ${response.code}")
            }

            val body = response.body
            val size = body.contentLength()

            body.byteStream().use { stream ->
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

        sha1Hash?.let {
            if (!verifyFileSHA1(destination, it)) {
                destination.deleteIfExists()
                setState(Task.State.FAILED)
                throw IllegalStateException("SHA-1 hash mismatch for downloaded file from $url, deleted file.")
            }
        }

        setState(Task.State.COMPLETED)
        setProgress(1.0f)

        destination
    }
}