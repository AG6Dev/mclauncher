package dev.ag6.mclauncher.utils.property

import javafx.beans.property.SimpleObjectProperty
import kotlin.reflect.KProperty

class FXObjectProperty<T>() {
    constructor(value: T) : this() {
        backingField.set(value)
    }

    private val backingField: SimpleObjectProperty<T> = SimpleObjectProperty()

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return backingField.get()
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        backingField.set(value)
    }

    fun toProperty(): SimpleObjectProperty<T> {
        return backingField
    }
}