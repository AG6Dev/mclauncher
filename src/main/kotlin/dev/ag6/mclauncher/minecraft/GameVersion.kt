package dev.ag6.mclauncher.minecraft

import com.google.gson.annotations.SerializedName

data class GameVersion(val id: String, val type: Type, val url: String, val time: String, val releaseTime: String) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GameVersion) return false

        if (id != other.id) return false
        if (type != other.type) return false
        if (url != other.url) return false
        if (time != other.time) return false
        if (releaseTime != other.releaseTime) return false

        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}

val UNKNOWN = GameVersion(
    id = "unknown",
    type = Type.RELEASE,
    url = "https://example.com/unknown",
    time = "1970-01-01T00:00:00Z",
    releaseTime = "1970-01-01T00:00:00Z"
)

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