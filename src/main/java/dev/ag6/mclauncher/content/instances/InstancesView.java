package dev.ag6.mclauncher.content.instances;

import dev.ag6.mclauncher.content.HasView;
import dev.ag6.mclauncher.instance.GameInstance;
import dev.ag6.mclauncher.instance.InstanceManager;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import lombok.NonNull;

//TODO: Eventually add a search bar, filtering options and a grid view
public class InstancesView implements HasView {
    private final AnchorPane root;

    public InstancesView(InstanceManager instManager) {
        this.root = new AnchorPane();
        this.root.setId("instances-root");
        // Temp as not finished

        this.root.getStylesheets().add("styles/instances.css");
        this.root.setStyle("-fx-text-fill: black");

        ListView<GameInstance> instanceListView = createInstanceListView(instManager);
        AnchorPane.setRightAnchor(instanceListView, 0.0);
        AnchorPane.setLeftAnchor(instanceListView, 0.0);
        AnchorPane.setBottomAnchor(instanceListView, 0.0);
        AnchorPane.setTopAnchor(instanceListView, 0.0);

        instanceListView.setFocusTraversable(false);
        instanceListView.setMouseTransparent(false);
        instanceListView.setSelectionModel(null);

        this.root.getChildren().add(instanceListView);
    }

    public ListView<GameInstance> createInstanceListView(InstanceManager instanceManager) {
        ListView<GameInstance> listView = new ListView<>();
        listView.setPlaceholder(new Label("No instances found."));
        listView.setId("instances-list-view");
        listView.setItems(instanceManager.getInstances());
        listView.setCellFactory(param -> new InstanceListCell());
        return listView;
    }

    @Override
    public @NonNull Parent getView() {
        return this.root;
    }
}
