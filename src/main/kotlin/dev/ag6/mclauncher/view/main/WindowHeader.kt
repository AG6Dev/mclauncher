package dev.ag6.mclauncher.view.main

import dev.ag6.mclauncher.MCLauncher
import dev.ag6.mclauncher.util.styleAs
import io.github.palexdev.materialfx.controls.MFXIconWrapper
import javafx.application.Platform
import javafx.css.PseudoClass
import javafx.event.EventHandler
import javafx.scene.control.Label
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.Region
import javafx.scene.paint.Color
import javafx.stage.Stage

class WindowHeader(private val stage: Stage) : HBox() {
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
                xOffset = stage.x - event.screenX
                yOffset = stage.y - event.screenY
            }
        }

        onMouseDragged = EventHandler { event ->
            if (canDrag) {
                stage.x = event.screenX + xOffset
                stage.y = event.screenY + yOffset
            }
        }

        children.addAll(title, spacer, )
    }

    private fun createWindowControl(iconId: String, color: Color): MFXIconWrapper =
        MFXIconWrapper("fas-circle", 15.0, color, 15.0).styleAs(iconId)

    private fun createButtonContainer(): HBox = HBox().styleAs("window-header-buttons").apply {
        children += createWindowControl("minimize-icon", Color.web("#FFBF37")).apply {
            onMousePressed = EventHandler { canDrag = false }
            onMouseReleased = EventHandler { _ ->
                canDrag = true
                stage.isIconified = true
            }
        }

        children += createWindowControl("always-on-top-icon", Color.web("#800080")).apply {
            onMousePressed = EventHandler { canDrag = false }
            onMouseReleased = EventHandler {
                canDrag = true
                val currentValue = stage.isAlwaysOnTop
                this.pseudoClassStateChanged(PseudoClass.getPseudoClass("always-on-top"), !currentValue)
                stage.isAlwaysOnTop = !currentValue
            }
        }

        children += createWindowControl("close-icon", Color.RED).apply {
            onMousePressed = EventHandler { canDrag = false }
            onMouseReleased = EventHandler { Platform.exit() }
        }
    }

    private fun createTitle(): Label = Label("MCLauncher " + MCLauncher.VERSION)
}