package dev.ag6.mclauncher.view.instance

import dev.ag6.mclauncher.instance.GameInstance
import dev.ag6.mclauncher.instance.InstanceManager
import dev.ag6.mclauncher.launch.InstanceLauncher
import dev.ag6.mclauncher.view.components.ConfirmActionWindow
import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.*
import javafx.scene.paint.Color

class InstanceListItem(private val instance: GameInstance) : HBox() {
    init {
        val infoBox = HBox().apply {
            children.addAll(Label(instance.name), Label(" - "), Label(instance.version()?.id))
        }

        val runButton = InstanceRunButton()
        val deleteButton = InstanceDeleteButton()

        val buttonContainer = HBox().apply {
            spacing = 10.0
            isVisible = false
            children.addAll(runButton, deleteButton)
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
