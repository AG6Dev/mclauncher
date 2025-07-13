package dev.ag6.mclauncher.utils

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
            stage.isResizable = resizable
            stage.isAlwaysOnTop = alwaysOnTop
            stage.icons += icon
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

    companion object {
        inline fun create(parent: Parent, block: WindowBuilder.() -> Unit): Window =
            WindowBuilder(parent).apply(block).build()
    }

    class WindowBuilder(val parent: Parent) : Builder<Window> {
        var title: String = "Window"
        var width: Double = 800.0
        var height: Double = 600.0
        var resizable: Boolean = true
        var alwaysOnTop: Boolean = false
        var style: StageStyle = StageStyle.DECORATED
        var icon: Image? = null

        override fun build(): Window = Window(this)
    }
}