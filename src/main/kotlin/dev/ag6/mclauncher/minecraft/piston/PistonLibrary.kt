package dev.ag6.mclauncher.minecraft.piston

import com.google.gson.JsonObject

data class PistonLibrary(val name: String, val download: Download?, val rules: List<LibraryRule>?) {
    fun getDirectory(): String {
        val parts = name.split(":")
        val group = parts[0].replace('.', '/')
        val artifact = parts[1]
        val version = parts[2]
        return "$group/$artifact/$version/"
    }

    fun getJarPath(): String {
        val parts = name.split(":")
        val group = parts[0].replace('.', '/')
        val artifact = parts[1]
        val version = parts[2]
        return "$group/$artifact/$version/$artifact-$version.jar"
    }

    companion object {
        fun fromJson(json: JsonObject): PistonLibrary {
            val name = json.get("name").asString
            var download: Download? = null
            if (json.has("downloads")) {
                val downloadsJson = json.getAsJsonObject("downloads")
                if (downloadsJson.has("artifact")) {
                    download = Download.fromJson(downloadsJson.getAsJsonObject("artifact"))
                }
            }

            var rules: List<LibraryRule>? = null
            if (json.has("rules")) {
                val rulesJson = json.getAsJsonArray("rules")
                rules = rulesJson.map { LibraryRule.fromJson(it.asJsonObject) }
            }
            return PistonLibrary(name, download, rules)
        }
    }

    data class LibraryRule(val action: Action, val os: OperatingSystem) {
        companion object {
            fun fromJson(json: JsonObject): LibraryRule {
                val action = when (json.get("action").asString) {
                    "allow" -> Action.ALLOW
                    "disallow" -> Action.DISALLOW
                    else -> throw IllegalArgumentException("Unknown action: ${json.get("action").asString}")
                }

                val os = if (json.has("os")) {
                    val osJson = json.getAsJsonObject("os")
                    OperatingSystem(osJson.get("name").asString)
                } else {
                    OperatingSystem("any")
                }

                return LibraryRule(action, os)
            }
        }

        enum class Action {
            ALLOW, DISALLOW
        }

        data class OperatingSystem(val name: String)
    }
}
