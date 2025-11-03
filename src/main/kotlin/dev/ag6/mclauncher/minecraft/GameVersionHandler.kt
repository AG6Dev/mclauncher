package dev.ag6.mclauncher.minecraft

import com.google.gson.JsonObject
import dev.ag6.mclauncher.MCLauncher
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import okhttp3.Request

object GameVersionHandler {
    private const val VERSION_MANIFEST_URL = "https://launchermeta.mojang.com/mc/game/version_manifest.json"

    val gameVersions: ObservableList<GameVersion> = FXCollections.observableArrayList()
    var latestVersion: GameVersion? = null
    var latestSnapshot: GameVersion? = null

    fun fetchGameVersions() {
        try {
            val request = Request.Builder().url(VERSION_MANIFEST_URL).get().build()

            val response = MCLauncher.HTTP_CLIENT.newCall(request).execute()
            response.use {
                if (!response.isSuccessful) {
                    error("Failed to fetch version manifest. Status code: ${response.code}")
                }

                val body = response.body.string()

                val jsonObject = MCLauncher.GSON.fromJson(body, JsonObject::class.java)
                val versions = jsonObject.getAsJsonArray("versions")

                for (jsonElement in versions) {
                    gameVersions.add(MCLauncher.GSON.fromJson(jsonElement, GameVersion::class.java))
                }

                val latest = jsonObject.getAsJsonObject("latest")
                val latestReleaseId = latest.asJsonObject.get("release").asString
                val latestSnapshotId = latest.asJsonObject.get("snapshot").asString

                latestVersion = gameVersions.find { it.id == latestReleaseId }
                latestSnapshot = gameVersions.find { it.id == latestSnapshotId }

                MCLauncher.LOGGER.info { "Fetched ${gameVersions.size} game versions." }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getVersion(id: String): GameVersion? {
        return gameVersions.find { it.id == id }
    }
}

