package dev.ag6.mclauncher.minecraft

import com.google.gson.annotations.SerializedName

data class GameVersion(val id: String, val type: Type, val url: String, val time: String, val releaseTime: String)

enum class Type {
    @SerializedName("release")
    RELEASE,

    @SerializedName("snapshot")
    SNAPSHOT,

    @SerializedName("old_beta")
    OLD_BETA,

    @SerializedName("old_alpha")
    OLD_ALPHA
}