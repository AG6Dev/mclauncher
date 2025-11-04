package dev.ag6.mclauncher.view.components

import dev.ag6.mclauncher.util.WindowCreator
import dev.ag6.mclauncher.view.ContentManager
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.stage.Modality
import javafx.stage.Stage

class ConfirmActionWindow(message: String, title: String = "Confirm Action", private val onConfirm: () -> Unit) {
    private val container = VBox()
    private val stage: Stage = WindowCreator.create {
        this.title = title
        modality = Modality.WINDOW_MODAL
        owner = ContentManager.stage
        resizable = false
        scene = Scene(container)
    }

    init {
        val buttonContainer = HBox().apply {
            padding = Insets(10.0, 10.0, 10.0, 10.0)
            alignment = Pos.BOTTOM_CENTER
        }

        val confirmButton = Button().apply {
            text = "Delete"
            setOnAction {
                onConfirm()
                WindowCreator.destroyWindow(stage)
            }
        }

        val cancelButton = Button().apply {
            text = "Cancel"
            setOnAction {
                WindowCreator.destroyWindow(stage)
            }
        }

        buttonContainer.apply {
            spacing = 10.0
            children.addAll(cancelButton, confirmButton)
        }

        container.padding = Insets(20.0, 20.0, 20.0, 20.0)
        container.children.addAll(Label(message), buttonContainer)

        stage.show()
    }
}