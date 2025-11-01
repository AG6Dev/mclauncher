package dev.ag6.mclauncher

import dev.ag6.ktconfig.Konfig
import dev.ag6.mclauncher.content.ContentManager
import dev.ag6.mclauncher.minecraft.GameVersionHandler
import dev.ag6.mclauncher.util.getDataLocation
import fr.brouillard.oss.cssfx.CSSFX
import io.github.oshai.kotlinlogging.KotlinLogging
import javafx.application.Application
import javafx.application.Application.launch
import javafx.stage.Stage
import kotlinx.coroutines.runBlocking
import java.nio.file.Files

class MCLauncher : Application() {
    private var firstStart: Boolean = false

    override fun init() {
        if (Files.notExists(getDataLocation())) {
            Files.createDirectories(getDataLocation())
            this.firstStart = true
        }

        Konfig.register(Config::class.java, getDataLocation().resolve("config.json"))

        GameVersionHandler.fetchGameVersions()
    }

    override fun start(primaryStage: Stage) {
        CSSFX.start()

        ContentManager.init(primaryStage)
        ContentManager.show()
    }

    override fun stop() = runBlocking {
        LOGGER.info { "Stopping MCLauncher..." }
    }

    companion object {
        const val VERSION: String = "1.0.0"
        val LOGGER = KotlinLogging.logger("MCLauncher")
    }
}

fun main() {
    launch(MCLauncher::class.java)
}