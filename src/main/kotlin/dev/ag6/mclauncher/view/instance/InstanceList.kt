package dev.ag6.mclauncher.view.instance

import dev.ag6.mclauncher.instance.GameInstance
import dev.ag6.mclauncher.instance.InstanceManager
import javafx.beans.binding.Bindings
import javafx.collections.FXCollections
import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import javafx.scene.control.ScrollPane
import javafx.scene.layout.VBox

class InstanceList : ScrollPane() {
    private val container = VBox()
    private val instances: ObservableList<GameInstance> = FXCollections.observableArrayList<GameInstance>().apply {
        Bindings.bindContent(this, InstanceManager.instances)
    }

    init {
        content = container
        isFitToWidth = true

        refreshItems()

        instances.addListener(ListChangeListener { refreshItems() })
    }

    private fun refreshItems() {
        container.children.clear()
        for(instance in instances) {
            val item = InstanceListItem(instance)
            container.children.add(item)
        }
    }
}