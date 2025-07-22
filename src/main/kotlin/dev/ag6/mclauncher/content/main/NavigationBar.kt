package dev.ag6.mclauncher.content.main

import dev.ag6.mclauncher.content.ViewBuilder
import dev.ag6.mclauncher.content.createinstance.CreateInstanceManager
import dev.ag6.mclauncher.utils.Window
import dev.ag6.mclauncher.utils.styleAs
import io.github.palexdev.materialfx.controls.MFXIconWrapper
import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.ToggleButton
import javafx.scene.control.ToggleGroup
import javafx.scene.layout.VBox

class NavigationBar(private val mainContent: MainController) : VBox() {
    private val selectedPageToggleGroup = ToggleGroup()

    init {
        styleAs("navigation-bar")
        stylesheets += "styles/navigation-bar.css"

        prefWidth = 200.0

//        children += createPageButton("Instances", "fas-cubes", InstancesView(mainContent.mcLauncher.instanceManager))
        children += createInstanceButton()
    }

    private fun createPageButton(msg: String, icon: String, viewBuilder: ViewBuilder?): ToggleButton =
        ToggleButton().styleAs("nav-button").apply {
            graphic = MFXIconWrapper(icon, 20.0, 20.0)
            text = msg
            alignment = Pos.CENTER_LEFT
            toggleGroup = selectedPageToggleGroup

            onAction = EventHandler {
                mainContent.changeContent(viewBuilder?.build())
            }
        } styleAs "nav-button"


    private fun createInstanceButton(): Button = Button().apply {
        graphic = MFXIconWrapper("fas-plus", 20.0, 20.0)
        text = "Create Instance"
        onAction = EventHandler {
            val window: Window = Window.create(CreateInstanceManager().build()) {
                title = "Create Instance"
                width = 600.0
                height = 400.0
                isResizable = false
            }

            window.showAndWait()
        }
    } styleAs "nav-button" styleAs "create-instance-button"
}