package dev.ag6.mclauncher.content.instances;

import dev.ag6.mclauncher.instance.GameInstance;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class InstanceListCell extends ListCell<GameInstance> {
    private final StackPane container = new StackPane();
    private final Label titleLabel = new Label();
    private final Label descriptionLabel = new Label();


    private final HBox buttonContainer = new HBox();
    private final Button editButton = new Button("", new MFXFontIcon("fas-pencil"));
    private final Button startButton = new Button("", new MFXFontIcon("fas-play"));
    private final FadeTransition fadeIn = new FadeTransition(Duration.millis(200), buttonContainer);

    public InstanceListCell() {
        this.getStylesheets().add("styles/instance-list-cell.css");

        createComponents();
        setupEventHandlers();
    }

    private void createComponents() {
        buttonContainer.setSpacing(5);

        editButton.getStyleClass().add("edit-button");
        startButton.getStyleClass().add("start-button");

        buttonContainer.getChildren().addAll(editButton, startButton);
        buttonContainer.setAlignment(Pos.CENTER_RIGHT);
        buttonContainer.getStyleClass().add("button-container");

        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.setAutoReverse(true);

        buttonContainer.setVisible(false);
        buttonContainer.setManaged(false);

        VBox infoContainer = new VBox();
        infoContainer.getChildren().addAll(titleLabel, descriptionLabel);
        infoContainer.setAlignment(Pos.CENTER_LEFT);

        StackPane.setAlignment(infoContainer, Pos.CENTER_LEFT);
        StackPane.setAlignment(buttonContainer, Pos.CENTER_RIGHT);

        container.getChildren().addAll(infoContainer, buttonContainer);
    }

    private void setupEventHandlers() {
        setOnMouseEntered(e -> {
            if (!isEmpty() && getItem() != null) {
                fadeIn.play();
                buttonContainer.setVisible(true);
                buttonContainer.setManaged(true);
            }
        });

        setOnMouseExited(e -> {
            buttonContainer.setVisible(false);
            buttonContainer.setManaged(false);
        });

        editButton.setOnAction(e -> {
            if (getItem() != null) {
                // Handle edit action
                System.out.println("Edit instance: " + getItem().name.get());
            }
        });

        startButton.setOnAction(e -> {
            if (getItem() != null) {
                // Handle start action
                System.out.println("Start instance: " + getItem().name.get());
            }
        });
    }

    @Override
    protected void updateItem(GameInstance item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            titleLabel.setText(item.name.get());
            descriptionLabel.setText(item.description.get());
            setGraphic(container);
        }
    }
}
