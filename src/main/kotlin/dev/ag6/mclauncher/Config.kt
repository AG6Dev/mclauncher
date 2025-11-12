package dev.ag6.mclauncher

import dev.ag6.konfig.Config
import dev.ag6.konfig.ConfigProperty
import dev.ag6.mclauncher.util.getDefaultDataLocation

@Config
class Config {
    @ConfigProperty
    var instancesDirectory: String = getDefaultDataLocation().resolve("instances").toString()

    @ConfigProperty
    var libraryDirectory: String = getDefaultDataLocation().resolve("libraries").toString()
}