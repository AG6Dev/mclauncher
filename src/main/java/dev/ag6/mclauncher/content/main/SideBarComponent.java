package dev.ag6.mclauncher.content.main;

import dev.ag6.mclauncher.content.HasView;
import dev.ag6.mclauncher.content.instances.InstancesView;
import io.github.palexdev.materialfx.controls.MFXIconWrapper;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

public class SideBarComponent extends VBox {
    private final ToggleGroup selectedPageGroup = new ToggleGroup();
    private final MainContentHandler handler;

    public SideBarComponent(MainContentHandler handler) {
        super();

        this.handler = handler;

        this.setId("sidebar");

        this.getStylesheets().add("styles/sidebar.css");
        this.setPrefWidth(200);

        ToggleButton instancesButton = this.createPageButton("Instances", "fas-house", new InstancesView(handler.getLauncher().getInstanceManager()));
        Button createInstanceButton = this.createInstanceButton();

        this.getChildren().addAll(instancesButton, createInstanceButton);
    }

    private ToggleButton createPageButton(String text, String icon, HasView newPage) {
        ToggleButton button = new ToggleButton(text);
        button.setGraphic(new MFXIconWrapper(icon, 20, 20));
        button.getStyleClass().addAll("nav-button", "nav-page-button");
        button.setAlignment(Pos.CENTER_LEFT);
        button.setOnAction(event -> this.handler.setContent(newPage));
        return button;
    }

    private Button createInstanceButton() {
        Button button = new Button("Create Instance", new MFXFontIcon("fas-plus"));
        button.getStyleClass().addAll("nav-button", "nav-create-instance-button");
        button.setOnAction(event -> this.handler.getLauncher().getInstanceManager().createInstance());
        return button;
    }

}
