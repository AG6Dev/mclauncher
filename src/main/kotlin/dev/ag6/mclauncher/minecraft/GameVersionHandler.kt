package dev.ag6.mclauncher.minecraft

import com.google.gson.Gson
import com.google.gson.JsonObject
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import okhttp3.*
import okio.IOException

object GameVersionHandler {
    private const val VERSION_MANIFEST_URL = "https://launchermeta.mojang.com/mc/game/version_manifest.json"
    private val httpClient = OkHttpClient()
    private val gson = Gson()

    val gameVersions: ObservableList<GameVersion> = FXCollections.observableArrayList()
    var latestVersion: GameVersion? = null
    var latestSnapshot: GameVersion? = null

    fun fetchGameVersions() {
        try {
            val request = Request.Builder().url(VERSION_MANIFEST_URL).get().build()

            httpClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        if (!response.isSuccessful) {
                            error("Failed to fetch version manifest. Status code: ${response.code}")
                        }

                        val body = response.body.string()

                        val jsonObject = gson.fromJson(body, JsonObject::class.java)
                        val versions = jsonObject.getAsJsonArray("versions")

                        for (jsonElement in versions) {
                            gameVersions.add(gson.fromJson(jsonElement, GameVersion::class.java))
                        }

                        val latest = jsonObject.getAsJsonObject("latest")
                        val latestReleaseId = latest.asJsonObject.get("release").asString
                        val latestSnapshotId = latest.asJsonObject.get("snapshot").asString

                        latestVersion = gameVersions.find { it.id == latestReleaseId }
                        latestSnapshot = gameVersions.find { it.id == latestSnapshotId }
                    }
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getVersion(id: String): GameVersion? {
        return gameVersions.find { it.id == id }
    }
}

