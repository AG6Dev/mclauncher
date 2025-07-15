package dev.ag6.mclauncher.utils

import javafx.event.EventHandler
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Stage
import javafx.stage.StageStyle
import javafx.util.Builder

class Window(builder: WindowBuilder) {
    val stage: Stage = Stage()
    private val scene: Scene

    init {
        with(builder) {
            stage.title = title
            stage.width = width
            stage.height = height
            stage.isResizable = isResizable
            stage.isAlwaysOnTop = isAlwaysOnTop
            if (builder.icon != null)
                stage.icons.add(icon)
            stage.onCloseRequest = EventHandler {
                onCloseRequest?.invoke()
            }
            stage.initStyle(style)

            scene = Scene(parent, width, height)
            stage.scene = scene
        }
    }

    fun show() {
        stage.show()
    }

    fun showAndWait() {
        stage.showAndWait()
    }

    fun hide() {
        stage.hide()
    }

    fun close() {
        stage.close()
    }

    companion object {
        inline fun create(parent: Parent, block: WindowBuilder.() -> Unit): Window =
            WindowBuilder(parent).apply(block).build()
    }

    class WindowBuilder(val parent: Parent) : Builder<Window> {
        var title: String = "Window"
        var width: Double = 800.0
        var height: Double = 600.0
        var isResizable: Boolean = true
        var isAlwaysOnTop: Boolean = false
        var style: StageStyle = StageStyle.DECORATED
        var icon: Image? = null
        var onCloseRequest: (() -> Unit)? = null

        override fun build(): Window = Window(this)
    }
}