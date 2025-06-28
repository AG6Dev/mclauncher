package dev.ag6.mclauncher.content.main;

import dev.ag6.mclauncher.MCLauncher;
import dev.ag6.mclauncher.content.HasView;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import lombok.Getter;

public class MainContentHandler implements HasView {
    @Getter
    private final MCLauncher launcher;

    private final BorderPane root;
    private final SideBarComponent sidebar;
    private final HeaderComponent header;

    public MainContentHandler(MCLauncher launcher) {
        this.launcher = launcher;

        this.root = new BorderPane();
        this.root.getStylesheets().add("styles/root.css");

        this.root.setPrefSize(1280, 720);
        this.root.setId("rootPane");
        this.root.setCenter(new Label("Test Label"));

        this.sidebar = new SideBarComponent(this);
        this.header = new HeaderComponent(launcher);

        this.root.setTop(header);
        this.root.setLeft(sidebar);
    }

    public void setContent(HasView newPage) {
        if (newPage == null) return;
        this.root.setCenter(newPage.getView());
    }

    @Override
    public Parent getView() {
        return this.root;
    }
}
