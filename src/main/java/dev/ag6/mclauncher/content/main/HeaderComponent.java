package dev.ag6.mclauncher.content.main;

import dev.ag6.mclauncher.MCLauncher;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.css.PseudoClass;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class HeaderComponent extends HBox {
    private final HBox buttons;
    private final MFXFontIcon closeIcon, minimiseIcon, alwaysOnTopIcon;
    private final Label titleLabel;

    private boolean canDrag = true;

    private double xOffset = 0;
    private double yOffset = 0;

    public HeaderComponent(MCLauncher launcher) {
        this.getStylesheets().add("styles/header.css");
        this.setId("header");

        this.buttons = new HBox();
        this.buttons.setId("headerButtons");

        this.closeIcon = createWindowControlIcon("closeIcon");
        this.alwaysOnTopIcon = createWindowControlIcon("alwaysOnTopIcon");
        this.minimiseIcon = createWindowControlIcon("minimiseIcon");

        this.closeIcon.setOnMousePressed(event -> this.canDrag = false);
        this.closeIcon.setOnMouseReleased(event -> launcher.getPrimaryStage().close());

        this.alwaysOnTopIcon.setOnMousePressed(event -> {
            this.canDrag = false;
            boolean isAlwaysOnTop = launcher.getPrimaryStage().isAlwaysOnTop();
            alwaysOnTopIcon.pseudoClassStateChanged(PseudoClass.getPseudoClass("always-on-top"), !isAlwaysOnTop);
            launcher.getPrimaryStage().setAlwaysOnTop(!isAlwaysOnTop);
        });
        this.alwaysOnTopIcon.setOnMouseReleased(event -> this.canDrag = true);

        this.minimiseIcon.setOnMouseReleased(event -> {
            this.canDrag = true;
            launcher.getPrimaryStage().setIconified(true);
        });
        this.minimiseIcon.setOnMousePressed(event -> this.canDrag = false);
        this.buttons.getChildren().addAll(minimiseIcon, alwaysOnTopIcon, closeIcon);

        this.titleLabel = new Label("MCLauncher " + MCLauncher.VERSION);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        this.setOnMousePressed(event -> {
            xOffset = launcher.getPrimaryStage().getX() - event.getScreenX();
            yOffset = launcher.getPrimaryStage().getY() - event.getScreenY();
        });

        this.setOnMouseDragged(event -> {
            if (!canDrag) {
                return;
            }

            launcher.getPrimaryStage().setX(event.getScreenX() + xOffset);
            launcher.getPrimaryStage().setY(event.getScreenY() + yOffset);
        });

        this.getChildren().addAll(this.titleLabel, spacer, this.buttons);
    }

    private MFXFontIcon createWindowControlIcon(String id) {
        var icon = new MFXFontIcon("fas-circle");
        icon.setId(id);
        icon.setSize(15);
        return icon;
    }
}
