package dev.ag6.mclauncher.content.instances

import dev.ag6.mclauncher.content.ViewBuilder
import dev.ag6.mclauncher.instance.GameInstance
import dev.ag6.mclauncher.instance.InstanceManager
import javafx.scene.control.Label
import javafx.scene.control.ListView
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.Region
import javafx.util.Callback

class InstancesView(private val instanceManager: InstanceManager) : ViewBuilder() {
    override fun build(): Region = AnchorPane().apply {
        id = "instances-root"
        stylesheets += "styles/instances.css"

        val instanceListView = createInstanceListView()
        AnchorPane.setTopAnchor(instanceListView, 0.0)
        AnchorPane.setBottomAnchor(instanceListView, 0.0)
        AnchorPane.setLeftAnchor(instanceListView, 0.0)
        AnchorPane.setRightAnchor(instanceListView, 0.0)
        children += instanceListView
    }

    fun createInstanceListView(): ListView<GameInstance> = ListView<GameInstance>().apply {
        id = "instances-list-view"
        placeholder = Label("No instances found.")
        items = instanceManager.instances
        cellFactory = Callback { InstanceListCell() }

        isFocusTraversable = false
        isMouseTransparent = false
        selectionModel = null

    }
}