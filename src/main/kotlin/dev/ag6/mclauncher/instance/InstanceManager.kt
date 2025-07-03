package dev.ag6.mclauncher.instance

import javafx.collections.FXCollections
import javafx.collections.ObservableList

class InstanceManager(private val instancesPath: String) {
    val instances: ObservableList<GameInstance> = FXCollections.observableArrayList<GameInstance>()

    init {
        for (i in 0..4) {
            val inst = GameInstance("Default Instance", "This is a default instance.")
            instances.add(inst)
        }
    }

    fun createInstance() {
        val newInstance = GameInstance("New Instance", "This is a new instance.")
        instances.add(newInstance)
    }
}
