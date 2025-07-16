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
                throw Exception("Failed to fetch version manifest. Status code: ${response.statusCode()}")
            }
        } catch (exception: Exception) {
            error("Error fetching game versions: ${exception.message}")
        }
    }

    private object ManifestTypeToken : TypeToken<List<GameVersion>>()
}

