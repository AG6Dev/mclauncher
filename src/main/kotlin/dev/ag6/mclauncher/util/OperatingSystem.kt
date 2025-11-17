package dev.ag6.mclauncher.util

enum class OperatingSystem {
    WINDOWS,
    MAC,
    LINUX,
    UNKNOWN;

    companion object {
        val CURRENT_OS = determineOs()

        private fun determineOs(): OperatingSystem {
            val osName = System.getProperty("os.name").lowercase()
            return when {
                osName.contains("win") -> WINDOWS
                osName.contains("mac") -> MAC
                osName.contains("nix") ||
                        osName.contains("nux") ||
                        osName.contains("aix") -> LINUX

                else -> UNKNOWN
            }
        }
    }
}