package dev.ag6.mclauncher.content.instances

import dev.ag6.mclauncher.instance.GameInstance
import dev.ag6.mclauncher.utils.styleAs
import io.github.palexdev.materialfx.controls.MFXIconWrapper
import javafx.animation.FadeTransition
import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ListCell
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.util.Duration

class InstanceListCell : ListCell<GameInstance>() {
    private val container = StackPane()

    private val titleLabel = Label()
    private val descriptionLabel = Label()

    private val buttonContainer = HBox()
    private val editButton = Button("", MFXIconWrapper("fas-pencil", 30.0, 30.0))
    private val playButton = Button("", MFXIconWrapper("fas-play", 30.0, 30.0))
    private val transition = FadeTransition(Duration.millis(200.0), buttonContainer)

    init {
        stylesheets += "styles/instance-list-cell.css"

        transition.apply {
            fromValue = 0.0
            toValue = 1.0
            isAutoReverse = true
        }

        editButton.styleAs("edit-button").apply {
            onAction = EventHandler { println("Edit instance: ${item?.name}") }

        }

        playButton.styleAs("play-button").apply {
            onAction = EventHandler { println("Play instance: ${item?.name}") }
        }

        onMouseEntered = EventHandler {
            if (!isEmpty && item != null) {
                transition.play()
                buttonContainer.isVisible = true
                buttonContainer.isManaged = true
            }
        }

        onMouseExited = EventHandler {
            if (!isEmpty && item != null) {
                buttonContainer.isVisible = false
                buttonContainer.isManaged = false
            }
        }

        buttonContainer.styleAs("button-container").apply {
            spacing = 5.0
            alignment = Pos.CENTER_RIGHT

            isVisible = false
            isManaged = false

            children.addAll(editButton, playButton)
        }

        val infoContainer = VBox().apply {
            alignment = Pos.CENTER_LEFT
            children.addAll(titleLabel, descriptionLabel)
        }

        StackPane.setAlignment(infoContainer, Pos.CENTER_LEFT)
        StackPane.setAlignment(buttonContainer, Pos.CENTER_RIGHT)

        container.children.addAll(infoContainer, buttonContainer)
    }

    override fun updateItem(item: GameInstance?, empty: Boolean) {
        super.updateItem(item, empty)
        if (empty || item == null) {
            text = null
            graphic = null
        } else {
            titleLabel.text = item.name
            descriptionLabel.text = item.description
            graphic = container
        }
    }
}