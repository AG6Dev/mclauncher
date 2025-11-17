package dev.ag6.mclauncher.util

import javafx.scene.Node
import java.awt.GraphicsEnvironment

infix fun <T : Node> T.styleAs(style: String): T = apply { styleClass += style }

infix fun <T : Node> T.addStyleSheet(styleSheet: String): T = apply {
    scene.stylesheets.add(styleSheet)
}

fun getRefreshRate(): Int {
    try {
        val gd = GraphicsEnvironment.getLocalGraphicsEnvironment().defaultScreenDevice
        val rate = gd.displayMode.refreshRate
        return if (rate > 0) rate else 60
    } catch (e: Exception) {
        return 60
    }
}