package dev.ag6.mclauncher.content.main

import dev.ag6.mclauncher.MCLauncher
import dev.ag6.mclauncher.content.ViewBuilder
import dev.ag6.mclauncher.content.instances.InstancesView
import dev.ag6.mclauncher.utils.styleAs
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Region

class MainController(private val launcher: MCLauncher) : ViewBuilder() {
    private val root: BorderPane = BorderPane()
    private val instances = InstancesView(launcher.instanceManager)

    override fun build(): Region = root.apply {
        setPrefSize(1280.0, 720.0)
        stylesheets += "styles/main.css"

        top = WindowHeader { launcher.window }
        left = NavigationBar(this@MainController)
        center = instances.root
    } styleAs "root-pane"

    fun changeContent(newContent: Region?) {
        root.center = newContent
    }
}