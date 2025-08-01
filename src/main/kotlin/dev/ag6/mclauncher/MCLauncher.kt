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
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

//TODO: First time setup screen
class MCLauncher : Application() {
    lateinit var instanceManager: InstanceManager
        private set

    lateinit var mainWindow: Window
        private set

    private var firstStart: Boolean = false

    override fun init() {
        this.firstStart = Files.exists(DATA_DIRECTORY) == false
        if (this.firstStart) {
            println("First start detected. Creating launcher data directory...")
            Files.createDirectories(DATA_DIRECTORY)
        }

        GameVersionHandler.fetchGameVersions()
        GameVersionHandler.loadAllMetadataFromDisk()

        this.instanceManager = InstanceManager(DATA_DIRECTORY)
        this.instanceManager.loadInstances()
    }

    override fun start(primaryStage: Stage) {
        CSSFX.start()

        with(primaryStage) {
            title = "MCLauncher $VERSION"
            isResizable = false
            icons += Image(MCLauncher::class.java.classLoader.getResourceAsStream("icon.png"))
            scene = Scene(MainController(this@MCLauncher).build()).apply { fill = Color.TRANSPARENT }

            scene.onKeyPressed = EventHandler {
                if (it.code == KeyCode.F1)
                    Desktop.getDesktop().open(DATA_DIRECTORY.toFile())
            }

            initStyle(StageStyle.TRANSPARENT)

            show()
        }

        this.mainWindow = Window(primaryStage)
    }

    override fun stop() {
        println("Stopping MCLauncher...")

        instanceManager.saveAllInstances()
    }


    private fun applyBorderToAll() {
        fun randomColor(): Color {
            return Color.color(Math.random(), Math.random(), Math.random())
        }

        mainWindow.scene.root.getAllDescendants().filterIsInstance<Region>()
            .forEach { it.border = Border(BorderStroke(randomColor(), BorderStrokeStyle.SOLID, null, null)) }
    }

    companion object {
        const val VERSION: String = "0.0.1"

        val DATA_DIRECTORY: Path = Paths.get(System.getProperty("user.home") + "/.mclauncher")
    }
}

fun main() {
    launch(MCLauncher::class.java)
}