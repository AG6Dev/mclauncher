package dev.ag6.mclauncher.content.createinstance

import dev.ag6.mclauncher.content.ViewBuilder
import dev.ag6.mclauncher.utils.styleAs
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.Region

class CreateInstanceManager : ViewBuilder() {
    override fun build(): Region = AnchorPane().styleAs("create-instance-root").apply {

    }
}