package dev.ag6.mclauncher.view.instance

import dev.ag6.mclauncher.instance.GameInstance
import javafx.scene.control.Label
import javafx.scene.layout.HBox

class InstanceListItem(instance: GameInstance): HBox() {
    init {
        children.addAll(Label(instance.name.get()), Label(" - "), Label(instance.version.get().id))
    }
}
