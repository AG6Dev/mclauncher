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

    override fun start(primaryStage: Stage) {
        CSSFX.start()

        this.primaryStage = primaryStage
        instanceManager = InstanceManager(this.getConfigDirectory())


        with(primaryStage) {
            title = "MCLauncher"
            scene = Scene(MainContent(this@MCLauncher).build()).apply {
                fill = Color.TRANSPARENT
            }
            isResizable = false
            icons.add(Image(MCLauncher::class.java.classLoader.getResourceAsStream("icon.png")))

            initStyle(StageStyle.TRANSPARENT)

            show()
        }
    }

    fun setIconified(value: Boolean) {
        primaryStage.isIconified = value
    }

    private fun getConfigDirectory(): String = System.getProperty("user.home") + "/.mclauncher"

    companion object {
        const val VERSION: String = "1.0.0"
    }
}

fun main() {
    launch(MCLauncher::class.java)
}