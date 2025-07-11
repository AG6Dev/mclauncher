package dev.ag6.mclauncher

import dev.ag6.mclauncher.content.main.MainContent
import dev.ag6.mclauncher.instance.InstanceManager
import fr.brouillard.oss.cssfx.CSSFX
import javafx.application.Application
import javafx.application.Application.launch
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.scene.paint.Color
import javafx.stage.Stage
import javafx.stage.StageStyle
import kotlinx.coroutines.runBlocking
import java.nio.file.Files
import java.nio.file.Paths

class MCLauncher : Application() {
    lateinit var instanceManager: InstanceManager
        private set

    lateinit var primaryStage: Stage

    var alwaysOnTop: Boolean = false
        get() = primaryStage.isAlwaysOnTop
        set(it) {
            primaryStage.isAlwaysOnTop = it
            field = it
        }

    private var firstStart: Boolean = false

    override fun start(primaryStage: Stage) {
        CSSFX.start()

        //TODO: Create necessary directories and config files etc.
        //TODO: First time setup screen
        this.firstStart =
            Files.exists(Paths.get(this.getDataDirectory())) == false

        this.primaryStage = primaryStage
        instanceManager = InstanceManager(this.getDataDirectory())

        with(primaryStage) {
            title = "MCLauncher"
            isResizable = false
            icons += Image(MCLauncher::class.java.classLoader.getResourceAsStream("icon.png"))
            scene = Scene(MainContent(this@MCLauncher).build()).apply { fill = Color.TRANSPARENT }

            initStyle(StageStyle.TRANSPARENT)

            show()
        }
    }

    override fun stop() {
        println("Stopping MCLauncher...")

        runBlocking {
            instanceManager.saveAllInstances()
        }
    }

    fun setIconified(value: Boolean) {
        primaryStage.isIconified = value
    }

    private fun getDataDirectory(): String = System.getProperty("user.home") + "/.mclauncher"

    companion object {
        const val VERSION: String = "1.0.0"
    }
}

fun main() {
    launch(MCLauncher::class.java)
}