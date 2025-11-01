package dev.ag6.mclauncher.view

import dev.ag6.mclauncher.MCLauncher
import dev.ag6.mclauncher.MCLauncher.Companion.VERSION
import dev.ag6.mclauncher.view.main.WindowHeader
import dev.ag6.mclauncher.util.WindowCreator
import dev.ag6.mclauncher.util.styleAs
import javafx.application.Platform
import javafx.scene.Scene
import javafx.scene.layout.BorderPane
import javafx.stage.Stage
import javafx.stage.StageStyle

object ContentManager {
    private val windowPane: BorderPane = BorderPane()
    private lateinit var windowHeader: WindowHeader

    private lateinit var stage: Stage
    private var currentView: View? = null

    fun init(stage: Stage) {
        this.stage = stage

        windowHeader = WindowHeader(stage)

        windowPane.styleAs("window-container")
        windowPane.top = windowHeader

        WindowCreator.create(stage) {
            title = "MCLauncher $VERSION"
            icon = MCLauncher::class.java.getResourceAsStream("icon.png")
            stageStyle = StageStyle.TRANSPARENT

            minWidth = 800
            minHeight = 600

            width = 1280
            height = 720

            scene = Scene(windowPane)
        }
    }

    fun changeView(newView: View) = Platform.runLater {
        currentView = newView
        windowPane.center = newView.build()
    }

    fun show() = Platform.runLater {
        stage.show()
    }
}