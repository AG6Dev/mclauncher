package dev.ag6.mclauncher.view.instance

import dev.ag6.mclauncher.view.ContentManager
import dev.ag6.mclauncher.view.View
import dev.ag6.mclauncher.view.create_instance.CreateInstanceView
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.Region

class InstancesView : View {
    private val container: BorderPane = BorderPane()
    private val instanceList: InstanceList = InstanceList()
    private val searchField: TextField = TextField()

    override fun build(): Region {
        val boxHeader = HBox().apply {
            spacing = 10.0
        }

        val searchField = searchField.apply {
            promptText = "Search instances..."
            onKeyTyped = EventHandler {
                val query = text.lowercase()
                instanceList.filterItems { instance ->
                    instance.name.get().lowercase().contains(query)
                }
            }
        }
        HBox.setHgrow(searchField, Priority.ALWAYS)

        val createInstanceButton = Button().apply {
            text = "Create Instance"
            onAction = EventHandler { ContentManager.changeView(CreateInstanceView()) }
            prefWidth = 150.0
        }

        boxHeader.children.addAll(searchField, createInstanceButton)

        BorderPane.setMargin(instanceList, Insets(10.0, 0.0, 0.0, 0.0))

        container.top = boxHeader
        container.center = instanceList

        return container
    }
}