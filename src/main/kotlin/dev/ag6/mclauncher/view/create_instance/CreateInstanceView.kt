package dev.ag6.mclauncher.view.create_instance

import dev.ag6.mclauncher.view.ContentManager
import dev.ag6.mclauncher.view.View
import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Region

class CreateInstanceView : View {
    private val container: BorderPane = BorderPane()

    override fun build(): Region {
        val backButton = Button().apply {
            text = "Cancel"
            prefWidth = 100.0
            onAction = EventHandler { ContentManager.goBack() }
        }

        val createButton: Button = Button().apply {
            text = "Create"
            prefWidth = 100.0
            onAction = EventHandler {

            }
        }

        val buttonContainer = HBox().apply {
            spacing = 10.0
            alignment = Pos.CENTER
        }
        buttonContainer.children.addAll(backButton, createButton)

        container.bottom = buttonContainer

        return container
    }
}