package dev.ag6.mclauncher.content.instances

import dev.ag6.mclauncher.instance.GameInstance
import dev.ag6.mclauncher.instance.InstanceManager
import dev.ag6.mclauncher.utils.styleAs
import javafx.scene.control.Label
import javafx.scene.control.ListView
import javafx.scene.layout.AnchorPane
import javafx.util.Callback

class InstancesView(private val instanceManager: InstanceManager) {
    val root: AnchorPane = AnchorPane().styleAs("instances-root").apply {
        stylesheets += "styles/instances.css"
        id = "instances-root"

        val instanceListView = createInstanceListView()
        children += instanceListView
        AnchorPane.setTopAnchor(instanceListView, 0.0)
        AnchorPane.setBottomAnchor(instanceListView, 0.0)
        AnchorPane.setLeftAnchor(instanceListView, 0.0)
        AnchorPane.setRightAnchor(instanceListView, 0.0)
    }

    private fun createInstanceListView(): ListView<GameInstance> = ListView<GameInstance>().apply {
        id = "instances-list-view"
        placeholder = Label("No instances found.")
        items = instanceManager.instances
        cellFactory = Callback { InstanceListCell() }

        isFocusTraversable = false
        isMouseTransparent = false
        selectionModel = null

    }
}