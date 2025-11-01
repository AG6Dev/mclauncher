package dev.ag6.mclauncher.view.instances

import dev.ag6.mclauncher.view.View
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Region

class InstancesView : View {
    private val container: BorderPane = BorderPane()
    private val instanceList: InstanceList = InstanceList()

    override fun build(): Region {
        val boxHeader = HBox()

        val searchField = TextField().apply {
            promptText = "Search instances..."
        }

        val createInstanceButton = Button()

        boxHeader.children.addAll(searchField, createInstanceButton)

        container.top = boxHeader
        container.center = instanceList

        return container
    }
}