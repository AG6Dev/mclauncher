package dev.ag6.mclauncher.launch

import dev.ag6.mclauncher.instance.GameInstance
import dev.ag6.mclauncher.minecraft.piston.PistonVersionMetadata
import java.io.File
import java.nio.file.Path
import kotlin.io.path.pathString


//TODO: Plan is to move launching game process, into a separate "project" so I can tr
class LaunchProcessBuilder(
    private val gameInstance: GameInstance,
    private val versionMetadata: PistonVersionMetadata,
    private val assetsLocation: Path,
    private val clientJar: Path,
    private val libraries: List<Path>
) {
    private val gameArgumentMap: MutableMap<String, String> = mutableMapOf()
    private val jvmArgumentMap: MutableMap<String, String> = mutableMapOf()

    init {
        gameArgumentMap["auth_player_name"] = "Player"
        gameArgumentMap["auth_uuid"] = ""
        gameArgumentMap["auth_access_token"] = ""
        gameArgumentMap["auth_xuid"] = ""

        gameArgumentMap["version_name"] = versionMetadata.id
        gameArgumentMap["gameDir"] = gameInstance.getMinecraftDirectory().toString()
        gameArgumentMap["asset_root"] = assetsLocation.toString()
        gameArgumentMap["assets_index_name"] = versionMetadata.assetIndex.id
        gameArgumentMap["clientid"] = versionMetadata.id

        gameArgumentMap["resolution_width"] = "1280"
        gameArgumentMap["resolution_height"] = "720"

        gameArgumentMap["quickPlayPath"] = ""
        gameArgumentMap["quickPlaySingleplayer"] = ""
        gameArgumentMap["quickPlayMultiplayer"] = ""
        gameArgumentMap["quickPlayRealms"] = ""

        jvmArgumentMap["natives_directory"] = gameInstance.getMinecraftDirectory().resolve("natives").toString()
        jvmArgumentMap["launcher_name"] = "MCLauncher"
        jvmArgumentMap["launcher_version"] = "1.0.0"
    }

    fun build(): Process {
        val javaExec = gameInstance.javaPath!!

        val processBuilder = ProcessBuilder(javaExec)
        processBuilder.directory(gameInstance.getMinecraftDirectory().toFile())
        processBuilder.command().add("-cp")
        processBuilder.command().add(buildClasspath())
        processBuilder.command().add(versionMetadata.mainClass)

        processBuilder.command().add("--version")
        processBuilder.command().add(versionMetadata.id)
        processBuilder.command().add("--accessToken")
        processBuilder.command().add("\"\"")
        processBuilder.command().add("--assetsDir")
        processBuilder.command().add(assetsLocation.toString())
        processBuilder.command().add("--assetsIndex")
        processBuilder.command().add(versionMetadata.assetIndex.id)

        processBuilder.inheritIO()

        return processBuilder.start()
    }

    private fun processGameArguments(argTemplate: String): String {
        return gameArgumentMap.entries.fold(argTemplate) { acc, (key, value) ->
            acc.replace("\${$key}", value)
        }
    }

    private fun buildClasspath(): String {
        val separator = File.pathSeparator
        val libCopy = libraries.toMutableList()
        libCopy.add(clientJar)
        return libCopy.map { it.pathString }.joinToString(separator) { it }.trimEnd(*separator.toCharArray())
    }
}