package dev.ag6.mclauncher.launch.tasks

import com.google.gson.JsonObject
import dev.ag6.mclauncher.MCLauncher
import dev.ag6.mclauncher.launch.InstanceLauncher
import dev.ag6.mclauncher.minecraft.MinecraftVersion
import dev.ag6.mclauncher.minecraft.piston.PistonVersionMetadata
import dev.ag6.mclauncher.task.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Request
import java.nio.file.Files

class GetVersionMetadataTask(private val minecraftVersion: MinecraftVersion) : Task<PistonVersionMetadata> {
    override val name: String = "Get Version Metadata"

    override suspend fun execute(): PistonVersionMetadata = withContext(Dispatchers.IO) {
        val metadataLocation = InstanceLauncher.METADATA_CACHE_LOCATION.resolve(minecraftVersion.id + ".json")

        if (Files.exists(metadataLocation)) {
            try {
                val metadata = Files.newBufferedReader(metadataLocation).use {
                    MCLauncher.GSON.fromJson(it, PistonVersionMetadata::class.java)
                }

                if (metadata.id == minecraftVersion.id) {
                    MCLauncher.LOGGER.info { "Using cached metadata for version ${minecraftVersion.id}" }
                    return@withContext metadata
                } else {
                    MCLauncher.LOGGER.warn { "Cached metadata version mismatch: ${metadata.id} != ${minecraftVersion.id}" }
                }
            } catch (e: Exception) {
                MCLauncher.LOGGER.warn(e) { "Failed to read cached metadata for version ${minecraftVersion.id}, re-downloading" }
            }
        }

        val request = Request.Builder().url(minecraftVersion.url).get().build()
        val response = MCLauncher.HTTP_CLIENT.newCall(request).execute()

        response.use {
            if (!response.isSuccessful) {
                throw Exception("Failed to download version metadata for version ${minecraftVersion.id}: ${response.code} ${response.message}")
            }

            val body = response.body.string()
            val json = MCLauncher.GSON.fromJson(body, JsonObject::class.java)
            val metadata = PistonVersionMetadata.fromJson(json)

            Files.createDirectories(InstanceLauncher.METADATA_CACHE_LOCATION)
            Files.newBufferedWriter(metadataLocation).use { writer ->
                writer.write(body)
            }

            metadata
        }
    }
}