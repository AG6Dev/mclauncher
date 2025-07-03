package dev.ag6.mclauncher.content.main

import dev.ag6.mclauncher.MCLauncher
import dev.ag6.mclauncher.content.ViewBuilder
import dev.ag6.mclauncher.utils.styleAs
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Region

class MainContent(val mcLauncher: MCLauncher) : ViewBuilder() {
    override fun build(): Region = BorderPane().styleAs("root-pane").apply {
        setPrefSize(1280.0, 720.0)
        stylesheets += "styles/main.css"

        top = WindowHeader(mcLauncher)
        left = NavigationBar(this@MainContent)
    }
}