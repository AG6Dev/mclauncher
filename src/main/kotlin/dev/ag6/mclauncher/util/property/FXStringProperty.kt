package dev.ag6.mclauncher.util.property

import javafx.beans.property.SimpleStringProperty
import kotlin.reflect.KProperty

class FXStringProperty() {
    constructor(value: String) : this() {
        property.set(value)
    }

    private val property: SimpleStringProperty = SimpleStringProperty()

    operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return this.property.get()
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String?) {
        this.property.set(value)
    }

    fun toProperty(): SimpleStringProperty {
        return property
    }
}

fun stringProperty(): FXStringProperty {
    return FXStringProperty()
}

fun stringProperty(value: String): FXStringProperty {
    return FXStringProperty(value)
}