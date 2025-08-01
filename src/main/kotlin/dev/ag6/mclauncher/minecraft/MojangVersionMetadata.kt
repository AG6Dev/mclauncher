package dev.ag6.mclauncher.minecraft

import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName

/**
 * Represents the version metadata in which is fetched from piston meta.
 */
data class MojangVersionMetadata(
    val arguments: MojangArguments,
    val assetIndex: MojangAssetIndex,
    val assets: String,
    val complianceLevel: Int,
    val downloads: MojangDownloads,
    val id: String,
    val javaVersion: MojangJavaVersion,
    val libraries: List<MojangLibrary>,
    val logging: MojangLogging,
    val mainClass: String,
    val minimumLauncherVersion: Int,
    val releaseTime: String,
    val time: String,
    val type: String
)

data class MojangArguments(
    val game: List<JsonElement>,
    val jvm: List<JsonElement>
)

data class MojangAssetIndex(val id: String, val sha1: String, val size: Int, val totalSize: Int, val url: String)

data class MojangDownloads(
    val client: MojangDownload? = null,
    val server: MojangDownload? = null,
    @SerializedName("client_mappings") val clientMappings: MojangDownload? = null,
    @SerializedName("server_mappings") val serverMappings: MojangDownload? = null
)

data class MojangDownload(
    val sha1: String,
    val size: Int,
    val url: String
)

data class MojangJavaVersion(val component: String, val majorVersion: Int)

data class MojangLibrary(
    val name: String,
    val downloads: MojangLibraryDownloads,
    val rules: List<MojangLibraryRule>? = null
) {
    data class MojangLibraryDownloads(val artifact: MojangLibraryArtifact) {
        data class MojangLibraryArtifact(val path: String, val sha1: String, val size: Int, val url: String)
    }

    data class MojangLibraryRule(
        val action: String,
        val os: MojangOSRule? = null,
    ) {
        data class MojangOSRule(val name: String)
    }
}

data class MojangLogging(val client: MojangLoggingClient) {
    data class MojangLoggingClient(val argument: String, val file: MojangLoggingFile, val type: String) {
        data class MojangLoggingFile(val id: String, val sha1: String, val size: Int, val url: String)
    }
}
