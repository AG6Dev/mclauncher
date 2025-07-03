package dev.ag6.mclauncher.utils

import javafx.scene.Node

fun <T : Node> T.styleAs(vararg style: String): T = apply { styleClass += style }