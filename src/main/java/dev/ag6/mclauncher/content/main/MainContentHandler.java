package dev.ag6.mclauncher.content.main;

import dev.ag6.mclauncher.MCLauncher;
import dev.ag6.mclauncher.content.HasView;
import io.github.palexdev.mfxresources.fonts.IconDescriptor;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.css.PseudoClass;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MainContentHandler implements HasView {
    private final BorderPane root;
    private final MCLauncher launcher;

    public MainContentHandler(MCLauncher launcher) {
        this.launcher = launcher;

        this.root = new BorderPane();
        this.root.getStylesheets().add("styles/root.css");

        this.root.setPrefSize(1280, 720);
        this.root.setId("rootPane");
        this.root.setCenter(new Label("Test Label"));

        var header = createHeader();
        this.root.setTop(header);
    }

    public HBox createHeader() {
        var header = new HBox();
        header.setId("rootHeader");

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

        header.getChildren().addAll(closeIcon, alwaysOnTopIcon, minimiseIcon);

        return header;
    }

    @Override
    public Parent getView() {
        return this.root;
    }

    private MFXFontIcon createWindowControlIcon(String id) {
        var icon = new MFXFontIcon("fas-circle");
        icon.setId(id);
        icon.setSize(20);
        return icon;
    }
}
