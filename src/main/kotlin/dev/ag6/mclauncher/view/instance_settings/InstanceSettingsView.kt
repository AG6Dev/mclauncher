package dev.ag6.mclauncher.view.instance_settings

import dev.ag6.mclauncher.instance.GameInstance
import dev.ag6.mclauncher.view.View
import dev.ag6.mclauncher.view.components.ContentBackButton
import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import javafx.scene.shape.Rectangle

class InstanceSettingsView(private val instance: GameInstance) : View {
    private val container = BorderPane()

    override fun build(): Region {
        val header = HBox().apply {
            alignment = Pos.BOTTOM_LEFT
            spacing = 10.0
        }
        val title = Label("Settings for ${instance.name}")
        header.children.addAll(Rectangle(64.0, 64.0), title)

        container.top = header

        val javaPath = TextField().apply {
            promptText = "Java Path"
            onKeyTyped = EventHandler {
                instance.javaPath = text
                instance.save()
            }
        }

        val optionsContainer = VBox().apply {
            children.addAll(javaPath)
        }


        container.bottom = ContentBackButton()
        container.center = optionsContainer

        return container
    }
}