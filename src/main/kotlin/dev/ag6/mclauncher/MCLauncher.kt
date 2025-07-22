package dev.ag6.mclauncher

import dev.ag6.mclauncher.content.main.MainController
import dev.ag6.mclauncher.instance.InstanceManager
import dev.ag6.mclauncher.minecraft.GameVersionHandler
import dev.ag6.mclauncher.utils.Window
import dev.ag6.mclauncher.utils.getAllDescendants
import fr.brouillard.oss.cssfx.CSSFX
import javafx.application.Application
import javafx.application.Application.launch
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.scene.input.KeyCode
import javafx.scene.layout.Border
import javafx.scene.layout.BorderStroke
import javafx.scene.layout.BorderStrokeStyle
import javafx.scene.layout.Region
import javafx.scene.paint.Color
import javafx.stage.Stage
import javafx.stage.StageStyle
import java.awt.Desktop
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

//TODO: First time setup screen
class MCLauncher : Application() {
    lateinit var instanceManager: InstanceManager
        private set

    lateinit var window: Window
        private set

    var alwaysOnTop: Boolean = false
        get() = window.stage.isAlwaysOnTop
        set(it) {
            window.stage.isAlwaysOnTop = it
            field = it
        }

    private var firstStart: Boolean = false

    override fun start(primaryStage: Stage) {
        CSSFX.start()

        val appDataDirectory = Paths.get(this.getAppDataDirectory())
        this.firstStart = Files.exists(appDataDirectory) == false
        if (this.firstStart) {
            println("First start detected. Creating app data directory...")
            Files.createDirectories(appDataDirectory)
        }

        this.instanceManager = InstanceManager(appDataDirectory)
        this.instanceManager.loadInstances()

        GameVersionHandler.fetchGameVersions()

        with(primaryStage) {
            title = "MCLauncher $VERSION"
            isResizable = false
            icons += Image(MCLauncher::class.java.classLoader.getResourceAsStream("icon.png"))
            scene = Scene(MainController(this@MCLauncher).build()).apply { fill = Color.TRANSPARENT }

            scene.onKeyPressed = EventHandler {
                if (it.code == KeyCode.F1)
                    Desktop.getDesktop().open(File(appDataDirectory.toString()))
            }

            initStyle(StageStyle.TRANSPARENT)

            show()
        }

        this.window = Window(primaryStage)
    }

    override fun stop() {
        println("Stopping MCLauncher...")

        instanceManager.saveAllInstances()
    }


    private fun applyBorderToAll() {
        fun randomColor(): Color {
            return Color.color(Math.random(), Math.random(), Math.random())
        }

        window.scene.root.getAllDescendants().filterIsInstance<Region>()
            .forEach { it.border = Border(BorderStroke(randomColor(), BorderStrokeStyle.SOLID, null, null)) }
    }

    private fun getAppDataDirectory(): String = System.getProperty("user.home") + "/.mclauncher"

    companion object {
        const val VERSION: String = "1.0.0"
    }
}

fun main() {
    launch(MCLauncher::class.java)
}