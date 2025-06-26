package dev.ag6.mclauncher;

import dev.ag6.mclauncher.instance.InstanceManager;
import dev.ag6.mclauncher.content.main.MainContentHandler;
import fr.brouillard.oss.cssfx.CSSFX;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;

public class MCLauncher extends Application {
    @Getter
    private Stage primaryStage;

    private final MainContentHandler contentHandler = new MainContentHandler(this);
    private InstanceManager instanceManager;

    @Override
    public void start(Stage primaryStage) {
        CSSFX.start();

        this.primaryStage = primaryStage;
        primaryStage.initStyle(StageStyle.TRANSPARENT);

        this.instanceManager = new InstanceManager(this.getConfigDirectory());

        var scene = new Scene(contentHandler.getView(), 1280, 720);
        scene.setFill(Color.TRANSPARENT);

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("MCLauncher");
        primaryStage.show();
    }

    private String getConfigDirectory() {
        return System.getProperty("user.home") + "/.custom-mclauncher/";
    }
}