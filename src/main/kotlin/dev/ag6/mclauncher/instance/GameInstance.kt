package dev.ag6.mclauncher.instance

import javafx.beans.property.SimpleStringProperty

class GameInstance(name: String, description: String) {
    private val _name = SimpleStringProperty(name)
    var name: String
        get() = _name.get()
        set(value) = _name.set(value)

    fun nameProperty() = _name

    private val _description = SimpleStringProperty(description)
    var description: String
        get() = _description.get()
        set(value) = _description.set(value)

    fun descriptionProperty() = _description
}
