package dev.ag6.mclauncher.content.instances;

import dev.ag6.mclauncher.instance.GameInstance;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;

public class InstanceListCell extends ListCell<GameInstance> {
    private final VBox container = new VBox();
    private final Label nameLabel = new Label();
    private final Label descriptionLabel = new Label();

    public InstanceListCell() {
        super();
        this.getStyleClass().add("instance-list-cell");

        this.container.getChildren().addAll(nameLabel, descriptionLabel);

        this.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue && this.getItem() == null) {
                this.getListView().getSelectionModel().select(this.getIndex());
            }
        });
    }

    @Override
    protected void updateItem(GameInstance item, boolean empty) {
        super.updateItem(item, empty);


        if(empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            this.nameLabel.textProperty().bind(item.name);
            this.descriptionLabel.textProperty().bind(item.description);
            setGraphic(container);
        }
    }
}
