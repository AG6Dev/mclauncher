package dev.ag6.mclauncher.content.main;

import dev.ag6.mclauncher.MCLauncher;
import dev.ag6.mclauncher.content.HasView;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.css.PseudoClass;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import lombok.Getter;

public class MainContentHandler implements HasView {
    @Getter
    private final MCLauncher launcher;

    private final BorderPane root;
    private final SideBarComponent sidebar;

    private double xOffset = 0;
    private double yOffset = 0;

    public MainContentHandler(MCLauncher launcher) {
        this.launcher = launcher;

        this.sidebar = new SideBarComponent(this);

        this.root = new BorderPane();
        this.root.getStylesheets().add("styles/root.css");

        this.root.setPrefSize(1280, 720);
        this.root.setId("rootPane");
        this.root.setCenter(new Label("Test Label"));

        var header = createHeader();

        this.root.setTop(header);
        this.root.setLeft(sidebar);
    }

    public HBox createHeader() {
        var header = new HBox();
        header.setId("rootHeader");

        header.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));

        var buttons = new HBox();
        buttons.setId("headerButtons");

        var closeIcon = createWindowControlIcon("closeIcon");
        var alwaysOnTopIcon = createWindowControlIcon("alwaysOnTopIcon");
        var minimiseIcon = createWindowControlIcon("minimiseIcon");

        closeIcon.setOnMouseClicked(event -> launcher.getPrimaryStage().close());
        alwaysOnTopIcon.setOnMouseClicked(event -> {
            boolean isAlwaysOnTop = launcher.getPrimaryStage().isAlwaysOnTop();
            alwaysOnTopIcon.pseudoClassStateChanged(PseudoClass.getPseudoClass("always-on-top"), !isAlwaysOnTop);
            launcher.getPrimaryStage().setAlwaysOnTop(!isAlwaysOnTop);
        });
        minimiseIcon.setOnMouseClicked(event -> launcher.getPrimaryStage().setIconified(true));
        buttons.getChildren().addAll(minimiseIcon, alwaysOnTopIcon, closeIcon);

        var title = new Label("MCLauncher | Version: " + MCLauncher.VERSION);
        title.setId("headerTitle");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        header.setOnMousePressed(event -> {
            xOffset = this.launcher.getPrimaryStage().getX() - event.getScreenX();
            yOffset = this.launcher.getPrimaryStage().getY() - event.getScreenY();
        });

        header.setOnMouseDragged(event -> {
            this.launcher.getPrimaryStage().setX(event.getScreenX() + xOffset);
            this.launcher.getPrimaryStage().setY(event.getScreenY() + yOffset);
        });

        header.getChildren().addAll(title, spacer, buttons);

        return header;
    }

    public void setContent(HasView newPage) {
        if(newPage == null) return;
        this.root.setCenter(newPage.getView());
    }

    @Override
    public Parent getView() {
        return this.root;
    }

    private MFXFontIcon createWindowControlIcon(String id) {
        var icon = new MFXFontIcon("fas-circle");
        icon.setId(id);
        icon.setSize(15);
        return icon;
    }
}
