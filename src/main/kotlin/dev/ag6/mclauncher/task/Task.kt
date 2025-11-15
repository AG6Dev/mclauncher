package dev.ag6.mclauncher.task

import javafx.beans.property.FloatProperty
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleFloatProperty
import javafx.beans.property.SimpleObjectProperty

interface Task<T> {
    val name: String
    val progressProperty: FloatProperty
        get() = SimpleFloatProperty(0.0f)
    val stateProperty: ObjectProperty<State>
        get() = SimpleObjectProperty(State.PENDING)

    suspend fun execute(): T

    fun isCancelled(): Boolean {
        return this.stateProperty.get() == State.CANCELLED
    }

    fun setProgress(progress: Float) {
        progressProperty.set(progress)
    }

    fun setState(state: State) {
        stateProperty.set(state)
    }

    fun cancel() {
        this.stateProperty.set(State.CANCELLED)
    }

    enum class State {
        PENDING,
        RUNNING,
        COMPLETED,
        FAILED,
        CANCELLED
    }
}