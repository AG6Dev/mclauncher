package dev.ag6.mclauncher.minecraft

import com.google.gson.JsonPrimitive


typealias AssetIndex = MojangAssetIndex

/**
 * Represents the version metadata which is saved to the disk.
 */
class LauncherVersionMetadata(mojangMeta: MojangVersionMetadata) {
    private val assetIndex: AssetIndex = mojangMeta.assetIndex
    private val compatibleJavaVersions: List<Int> = listOf(mojangMeta.javaVersion.majorVersion)
    private val logging: MojangLogging.MojangLoggingClient = mojangMeta.logging.client
    private val mainClass: String = mojangMeta.mainClass
    private val clientJar: MojangDownload =
        mojangMeta.downloads.client ?: throw IllegalArgumentException("Client download is missing")
    private val gameArguments: List<String> =
        mojangMeta.arguments.game.filter { it.isJsonPrimitive }.map { (it as JsonPrimitive).asString }
    private val version: String = mojangMeta.id
    private val type: Type = Type.valueOf(mojangMeta.type.uppercase())
    private val libraries: List<MojangLibrary> = mojangMeta.libraries
}