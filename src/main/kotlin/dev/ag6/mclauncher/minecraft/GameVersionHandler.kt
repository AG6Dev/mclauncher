package dev.ag6.mclauncher.minecraft

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import dev.ag6.mclauncher.MCLauncher
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.file.Files
import java.nio.file.Path

object GameVersionHandler {
    private const val VERSION_MANIFEST_URL = "https://launchermeta.mojang.com/mc/game/version_manifest.json"
    private val VERSION_META_PATH = MCLauncher.DATA_DIRECTORY.resolve("meta")
    private val httpClient = HttpClient.newHttpClient()
    private val gson = GsonBuilder().setPrettyPrinting().create()

    val allVersions: ObservableList<GameVersion> = FXCollections.observableArrayList()
    private val versionMetas: MutableMap<GameVersion, LauncherVersionMetadata> = mutableMapOf()

    //TODO: coroutines
    fun fetchGameVersions() {
        try {
            val request = HttpRequest.newBuilder().uri(URI.create(VERSION_MANIFEST_URL)).GET().build()
            val response: HttpResponse<String> = httpClient.send(request, HttpResponse.BodyHandlers.ofString())

            if (response.statusCode() == 200) {
                val manifest: JsonObject = gson.fromJson(response.body(), JsonObject::class.java)
                val versions: List<GameVersion> = gson.fromJson(manifest.get("versions"), ManifestTypeToken)
                allVersions.addAll(versions)

                println("Fetched ${versions.size} game versions.")
            } else {
                throw IllegalStateException("Failed to fetch version manifest. Status code: ${response.statusCode()}")
            }
        } catch (exception: Exception) {
            error("Error fetching game versions: ${exception.message}")
        }
    }

    //TODO: coroutine this
    fun loadAllMetadataFromDisk() {
        try {
            Files.createDirectories(VERSION_META_PATH)

            val minecraftRoot = VERSION_META_PATH.resolve("net.minecraft")
            if (Files.exists(minecraftRoot)) {
                val versionFolders: List<Path> = Files.list(minecraftRoot).filter(Files::isDirectory).toList()

                for (folder in versionFolders) {
                    val versionId = folder.fileName.toString()
                    val versionFile = folder.resolve("$versionId.json")

                    val gameVersion = allVersions.find { it.id == versionId } ?: continue
                    if (Files.exists(versionFile)) {
                        val versionMeta = Files.readString(versionFile)
                        val launcherMetadata = gson.fromJson(versionMeta, LauncherVersionMetadata::class.java)
                        versionMetas[gameVersion] = launcherMetadata
                    }
                }
            }
        } catch (e: Exception) {
            error("Error loading cached version metadata from disk: ${e.message}")
        }
    }

    private fun addVersionMeta(version: GameVersion, metadata: LauncherVersionMetadata) {
        try {
            if (version == UNKNOWN) return
            versionMetas[version] = metadata

            val versionMetaFile =
                VERSION_META_PATH.resolve("net.minecraft").resolve(version.id).resolve("${version.id}.json")

            Files.createDirectories(versionMetaFile.parent)
            Files.writeString(versionMetaFile, gson.toJson(metadata))
        } catch (e: Exception) {
            TODO("Not yet implemented")
        }
    }

    /**
     * Fetches and caches the metadata for a specific game version.
     */
    fun fetchVersionMeta(version: GameVersion): LauncherVersionMetadata? {
        try {
            if (version == UNKNOWN) return null

            if (versionMetas[version] != null) {
                return versionMetas[version]
            }

            val request = HttpRequest.newBuilder().uri(URI.create(version.url)).GET().build()


            val response: HttpResponse<String> = httpClient.send(request, HttpResponse.BodyHandlers.ofString())

            if (response.statusCode() == 200) {
                val metadata = gson.fromJson(response.body(), MojangVersionMetadata::class.java)

                val launcherMetadata = LauncherVersionMetadata(metadata)
                addVersionMeta(version, launcherMetadata)

                return launcherMetadata
            } else {
                error("Failed to fetch version metadata for ${version.id}. Status code: ${response.statusCode()}")
            }
        } catch (exception: Exception) {
            error("Error fetching version metadata for ${version.id}: ${exception.message}")
        }
    }

    fun getVersionByString(id: String?): GameVersion? {
        return allVersions.find { it.id == id }
    }

    private object ManifestTypeToken : TypeToken<List<GameVersion>>()
}

