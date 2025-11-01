package dev.ag6.mclauncher

import dev.ag6.ktconfig.Config
import dev.ag6.ktconfig.ConfigProperty

@Config
class Config {
    @ConfigProperty
    var autoCheckForUpdates: Boolean = true
}