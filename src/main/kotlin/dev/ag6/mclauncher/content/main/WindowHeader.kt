package dev.ag6.mclauncher.content.main

import dev.ag6.mclauncher.MCLauncher
import dev.ag6.mclauncher.utils.styleAs
import io.github.palexdev.materialfx.controls.MFXIconWrapper
import javafx.application.Platform
import javafx.css.PseudoClass
import javafx.event.EventHandler
import javafx.scene.control.Label
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import javafx.scene.paint.Color

class WindowHeader(private val launcher: MCLauncher) : HBox() {
    private var xOffset = 0.0
    private var yOffset = 0.0

    private var canDrag = true

    init {
        styleAs("window-header")
        stylesheets += "styles/window-header.css"

        val buttonContainer = createButtonContainer()
        val title = createTitle()
        val spacer = Region()
        setHgrow(spacer, Priority.ALWAYS)

        onMousePressed = EventHandler { event ->
            if (canDrag) {
                xOffset = launcher.primaryStage.x - event.screenX
                yOffset = launcher.primaryStage.y - event.screenY
            }
        }

        onMouseDragged = EventHandler { event ->
            if (canDrag) {
                launcher.primaryStage.x = event.screenX + xOffset
                launcher.primaryStage.y = event.screenY + yOffset
            }
        }

        children.addAll(title, spacer, buttonContainer)
    }

    private fun createWindowControl(iconId: String, color: Color): MFXIconWrapper =
        MFXIconWrapper("fas-circle", 15.0, color, 15.0).styleAs(iconId)

    private fun createButtonContainer(): HBox = HBox().styleAs("window-header-buttons").apply {
        children += createWindowControl("minimize-icon", Color.web("#FFBF37")).apply {
            onMousePressed = EventHandler { canDrag = false }
            onMouseReleased = EventHandler { _ ->
                canDrag = true
                launcher.setIconified(true)
            }
        }

        children += createWindowControl("always-on-top-icon", Color.web("#800080")).apply {
            onMousePressed = EventHandler { canDrag = false }
            onMouseReleased = EventHandler {
                canDrag = true
                val currentValue = launcher.alwaysOnTop
                this.pseudoClassStateChanged(PseudoClass.getPseudoClass("always-on-top"), !currentValue)
                launcher.alwaysOnTop = !currentValue
            }
        }

        children += createWindowControl("close-icon", Color.RED).apply {
            onMousePressed = EventHandler { canDrag = false }
            onMouseReleased = EventHandler { Platform.exit() }
        }
    }

    private fun createTitle(): Label = Label("MCLauncher " + MCLauncher.VERSION)
}