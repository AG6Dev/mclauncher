package dev.ag6.mclauncher.utils

import javafx.scene.Node
import javafx.scene.Parent

infix fun <T : Node> T.styleAs(style: String): T = apply { styleClass += style }

fun Parent.getAllDescendants(): List<Node> {
    val descendents = mutableListOf<Node>()
    descendents.addAll(this.childrenUnmodifiable)
    for (child in this.childrenUnmodifiable) {
        if (child is Parent) {
            descendents.addAll(child.getAllDescendants())
        }
    }
    return descendents
}

