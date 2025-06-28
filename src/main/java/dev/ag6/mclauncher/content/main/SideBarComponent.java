package dev.ag6.mclauncher.content.main;

import dev.ag6.mclauncher.content.HasView;
import dev.ag6.mclauncher.content.instances.InstancesView;
import io.github.palexdev.materialfx.controls.MFXIconWrapper;
import io.github.palexdev.materialfx.controls.MFXRectangleToggleNode;
import javafx.geometry.Pos;
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

        this.getChildren().add(createPageButton("Instances", "fas-house", new InstancesView(handler.getLauncher().getInstanceManager())));
    }

    private MFXRectangleToggleNode createPageButton(String text, String icon, HasView newPage) {
        var wrapper = new MFXIconWrapper(icon, 24, 32);
        var btn = new MFXRectangleToggleNode(text, wrapper);

        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setToggleGroup(selectedPageGroup);
        btn.setOnAction(event -> this.handler.setContent(newPage));

        return btn;
    }

}
