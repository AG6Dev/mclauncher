package dev.ag6.mclauncher.minecraft

import com.google.gson.annotations.SerializedName

data class MinecraftVersion(
    val id: String,
    val type: Type,
    val url: String,
    val time: String,
    val releaseTime: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MinecraftVersion) return false

        if (id != other.id) return false
        return true
    }

    override fun toString(): String {
        return "$id ($type)"
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}

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