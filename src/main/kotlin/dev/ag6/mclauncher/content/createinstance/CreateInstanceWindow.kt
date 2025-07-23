package dev.ag6.mclauncher.content.createinstance

import dev.ag6.mclauncher.content.ViewBuilder
import dev.ag6.mclauncher.instance.InstanceManager
import dev.ag6.mclauncher.minecraft.GameVersionHandler
import dev.ag6.mclauncher.minecraft.UNKNOWN
import dev.ag6.mclauncher.utils.Window
import dev.ag6.mclauncher.utils.styleAs
import javafx.collections.FXCollections
import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.TextField
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.Region

class CreateInstanceWindow(private val instanceManager: InstanceManager) : ViewBuilder() {
    private val window = Window.create(this.build()) {
        title = "Create Instance"
        width = 600.0
        height = 400.0
        isResizable = false
    }

    init {
        window.show()
    }

    override fun build(): Region = AnchorPane().apply {

        val nameField = TextField().apply {
            promptText = "Instance Name"
            layoutX = 20.0
            layoutY = 20.0
            prefWidth = 560.0
        }

        val descriptionField = TextField().apply {
            promptText = "Instance Description"
            layoutX = 20.0
            layoutY = 60.0
            prefWidth = 560.0
        }

        val versionField = ComboBox<String>().apply {
            val verStrings =
                GameVersionHandler.allVersions.map { it.id }.toCollection(FXCollections.observableArrayList())

            promptText = "Game Version"
            layoutX = 20.0
            layoutY = 100.0
            prefWidth = 560.0
            items = verStrings

            selectionModel.selectFirst()
        }

        children.addAll(
            nameField,
            descriptionField,
            versionField
        )

        children += Button().apply {
            text = "Create"
            onAction = EventHandler {
                instanceManager.createInstance(
                    nameField.text.ifBlank { "Unnamed Instance" },
                    descriptionField.text.ifBlank { "No description" },
                    GameVersionHandler.getVersionByString(versionField.value) ?: UNKNOWN
                )
                window.close()
            }
        }
    } styleAs "create-instance-root"
}