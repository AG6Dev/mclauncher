package dev.ag6.mclauncher.minecraft

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

object GameVersionHandler {
    private const val VERSION_MANIFEST_URL = "https://launchermeta.mojang.com/mc/game/version_manifest.json"
    private val httpClient = HttpClient.newHttpClient()
    private val gson = Gson()

    val allVersions: ObservableList<GameVersion> = FXCollections.observableArrayList()

    fun fetchGameVersions() {
        val request = HttpRequest.newBuilder()
            .uri(URI.create(VERSION_MANIFEST_URL))
            .GET()
            .build()

        try {
            val response: HttpResponse<String> =
                httpClient.send(request, HttpResponse.BodyHandlers.ofString())

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

    /**
     * Fetches and caches the metadata for a specific game version.
     */
//    fun fetchVersionMeta(version: GameVersion): VersionMetadata? {
//        if (version == UNKNOWN) return null
//
//        val request = HttpRequest.newBuilder()
//            .uri(URI.create(version.url))
//            .GET()
//            .build()
//
//        return try {
//            val response: HttpResponse<String> = httpClient.send(request, HttpResponse.BodyHandlers.ofString())
//
//            if (response.statusCode() == 200) {
//                val metadata: JsonObject =
//                    gson.fromJson(response.body(), JsonObject::class.java)
//
//                return VersionMetadata.fromJson(metadata)
//            } else {
//                error("Failed to fetch version metadata for ${version.id}. Status code: ${response.statusCode()}")
//                null
//            }
//        } catch (exception: Exception) {
//            error("Error fetching version metadata for ${version.id}: ${exception.message}")
//        }
//    }

    fun getVersionByString(id: String?): GameVersion? {
        return allVersions.find { it.id == id }
    }

    private object ManifestTypeToken : TypeToken<List<GameVersion>>()
}

