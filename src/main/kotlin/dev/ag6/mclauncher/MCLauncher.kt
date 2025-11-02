package dev.ag6.mclauncher

import com.google.gson.Gson
import dev.ag6.ktconfig.Konfig
import dev.ag6.mclauncher.view.ContentManager
import dev.ag6.mclauncher.minecraft.GameVersionHandler
import dev.ag6.mclauncher.util.getDataLocation
import dev.ag6.mclauncher.util.getRefreshRate
import dev.ag6.mclauncher.view.instance.InstancesView
import fr.brouillard.oss.cssfx.CSSFX
import io.github.oshai.kotlinlogging.KotlinLogging
import javafx.application.Application
import javafx.application.Application.launch
import javafx.stage.Stage
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
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
        ContentManager.changeView(InstancesView())
        ContentManager.show()
    }

    override fun stop() = runBlocking {
        HTTP_CLIENT.dispatcher.executorService.shutdown()
    }

    companion object {
        const val VERSION: String = "1.0.0"

        val LOGGER = KotlinLogging.logger("MCLauncher")
        val HTTP_CLIENT = OkHttpClient()
        val GSON = Gson()
    }
}

fun main() {
    System.setProperty("javafx.animation.pulse", getRefreshRate().toString())
    launch(MCLauncher::class.java)
}