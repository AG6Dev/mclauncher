package dev.ag6.mclauncher.view.instance

import dev.ag6.mclauncher.instance.GameInstance
import dev.ag6.mclauncher.instance.InstanceManager
import dev.ag6.mclauncher.launch.InstanceLauncher
import dev.ag6.mclauncher.view.ContentManager
import dev.ag6.mclauncher.view.components.ConfirmActionWindow
import dev.ag6.mclauncher.view.instance_settings.InstanceSettingsView
import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle

class InstanceListItem(private val instance: GameInstance) : HBox() {
    init {
        //TODO: replace with actual icon
        val icon = Rectangle(48.0, 48.0)

        val titleBox = HBox(Label(instance.name))
        val versionBox = HBox(Label(instance.version()?.id))
        val infoVBox = VBox().apply {
            alignment = Pos.CENTER
            children.addAll(titleBox, versionBox)
        }

        val infoBox = HBox().apply {
            spacing = 10.0
            alignment = Pos.CENTER
            children.addAll(icon, infoVBox)
        }


        val runButton = InstanceRunButton()
        val deleteButton = InstanceDeleteButton()
        val configButton = Button("Settings").apply {
            onAction = EventHandler {
                ContentManager.changeView(InstanceSettingsView(instance))
            }
        }

        val buttonContainer = HBox().apply {
            spacing = 10.0
            isVisible = false
            alignment = Pos.CENTER
            children.addAll(configButton, runButton, deleteButton)
        }

        val spacer = Region().apply {
            setHgrow(this, Priority.ALWAYS)
        }

        setOnMouseEntered {
            border = Border(BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT))
            buttonContainer.isVisible = true
        }
        setOnMouseExited {
            border = Border.EMPTY
            buttonContainer.isVisible = false
        }

        children.addAll(infoBox, spacer, buttonContainer)
    }

    private inner class InstanceRunButton : Button() {
        init {
            text = "Run"
            onAction = EventHandler {
                InstanceLauncher.launchInstance(instance)
            }
        }
    }

    private inner class InstanceDeleteButton : Button() {
        init {
            text = "Delete"
            onAction = EventHandler {
                ConfirmActionWindow("Are you sure you want to delete the instance \"${instance.name}\"? This action cannot be undone.") {
                    InstanceManager.deleteInstance(
                        instance
                    )
                }
            }
        }
    }
}
