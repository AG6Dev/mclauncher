package dev.ag6.mclauncher

import dev.ag6.ktconfig.Config
import dev.ag6.ktconfig.ConfigProperty
import dev.ag6.mclauncher.util.getDefaultDataLocation

@Config
class Config {
    @ConfigProperty
    var instancesDirectory: String = getDefaultDataLocation().toString()
}