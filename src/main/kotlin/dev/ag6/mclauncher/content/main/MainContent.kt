package dev.ag6.mclauncher.content.main

import dev.ag6.mclauncher.MCLauncher
import dev.ag6.mclauncher.content.ViewBuilder
import dev.ag6.mclauncher.content.instances.InstancesView
import dev.ag6.mclauncher.utils.styleAs
import javafx.scene.layout.*
import javafx.scene.paint.Color

class MainContent(val mcLauncher: MCLauncher) : ViewBuilder() {
    val root: BorderPane = BorderPane()
    val instances = InstancesView(mcLauncher.instanceManager)

    override fun build(): Region = root.styleAs("root-pane").apply {
        setPrefSize(1280.0, 720.0)
        stylesheets += "styles/main.css"

        top = WindowHeader(mcLauncher).apply {
            root.border = Border(BorderStroke(Color.RED, BorderStrokeStyle.SOLID, null, null))
        }
        left = NavigationBar(this@MainContent)
        center = instances.root
    }

    fun changeContent(content: ViewBuilder?) {
        root.center = content?.build()
    }
}