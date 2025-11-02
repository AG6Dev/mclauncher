package dev.ag6.mclauncher.view

import ch.micheljung.fxwindow.FxStage
import dev.ag6.mclauncher.MCLauncher
import dev.ag6.mclauncher.MCLauncher.Companion.VERSION
import dev.ag6.mclauncher.util.WindowCreator
import dev.ag6.mclauncher.view.main.WindowHeader
import javafx.application.Platform
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.layout.BorderPane
import javafx.scene.layout.StackPane
import javafx.stage.Stage
import javafx.stage.StageStyle

object ContentManager {
    private val windowPane: BorderPane = BorderPane()
    private lateinit var stage: Stage
    private val navigationStack: ArrayDeque<View> = ArrayDeque()

    fun init(stage: Stage) {
        this.stage = stage

        windowPane.apply {
            padding = Insets(10.0, 10.0, 10.0, 10.0)
        }

        WindowCreator.create(stage) {
            title = "MCLauncher $VERSION"
            icon = MCLauncher::class.java.getResourceAsStream("icon.png")
//            stageStyle = StageStyle.TRANSPARENT

            minWidth = 800
            minHeight = 600

            width = 1280
            height = 720

            scene = Scene(windowPane)
        }
    }

    fun changeView(newView: View) = Platform.runLater {
        navigationStack.addLast(newView)
        windowPane.center = newView.build()
    }

    fun goBack() = Platform.runLater {
        if (navigationStack.size <= 1) return@runLater

        navigationStack.removeLast()
        val previousView = navigationStack.last()
        windowPane.center = previousView.build()
    }

    fun show() = Platform.runLater {
        stage.show()
    }
}