package dev.ag6.mclauncher.util

import javafx.scene.Node

infix fun <T : Node> T.styleAs(style: String): T = apply { styleClass += style }
