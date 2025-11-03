package dev.ag6.mclauncher.util

import dev.ag6.mclauncher.MCLauncher
import java.io.InputStream
import java.net.URL
import java.nio.file.Path
import java.nio.file.Paths

fun getDefaultDataLocation(): Path {
    val userHome = System.getProperty("user.home")
    val osName = System.getProperty("os.name").lowercase()

    return when {
        osName.contains("win") -> Paths.get(System.getenv("APPDATA"), "MCLauncher")
        osName.contains("mac") -> Paths.get(userHome, "Library", "Application Support", "MCLauncher")
        else -> Paths.get(userHome, ".mclauncher")
    }
}

fun getResourceStream(path: String): InputStream? {
    return MCLauncher::class.java.getResourceAsStream(path)
}

fun getResource(path: String): URL? {
    return MCLauncher::class.java.getResource(path)
}