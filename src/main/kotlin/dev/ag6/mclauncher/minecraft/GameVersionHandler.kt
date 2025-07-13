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

//consider changing this to an object class
class GameVersionHandler {
    private val allVersions: ObservableList<GameVersion> = FXCollections.observableArrayList()

    fun fetchGameVersions() {
        val request = HttpRequest.newBuilder()
            .uri(URI.create(versionManifestUrl))
            .GET()
            .build()

        try {
            val response: HttpResponse<String> =
                httpClient.send(request, HttpResponse.BodyHandlers.ofString())

            if (response.statusCode() == 200) {
                val manifest: JsonObject = gson.fromJson(response.body(), JsonObject::class.java)
                val versions: List<GameVersion> = gson.fromJson(manifest.get("versions"), ManifestTypeToken)
                allVersions.addAll(versions)

                for (i in 0..5) {
                    println("Version ${i}: ${versions[i].id} - ${versions[i].type} - ${versions[i].url}")
                }

                println("Fetched ${versions.size} game versions.")
            } else {
                throw Exception("Failed to fetch version manifest. Status code: ${response.statusCode()}")
            }
        } catch (exception: Exception) {
            error("Error fetching game versions: ${exception.message}")
        }
    }


    companion object {
        private const val versionManifestUrl: String = "https://launchermeta.mojang.com/mc/game/version_manifest.json"
        private val httpClient: HttpClient = HttpClient.newHttpClient()
        private val gson: Gson = Gson()
    }

    private object ManifestTypeToken : TypeToken<List<GameVersion>>()
}

