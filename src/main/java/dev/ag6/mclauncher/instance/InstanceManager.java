package dev.ag6.mclauncher.instance;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;

public class InstanceManager {
    private final String instancesPath;

    @Getter private final ObservableList<GameInstance> instances = FXCollections.observableArrayList();


    public InstanceManager(String instancesPath) {
        this.instancesPath = instancesPath;

        for (int i = 0; i < 5; i++) {
            var inst = new GameInstance("Default Instance", "This is a default instance.");
            this.instances.add(inst);
        }
    }
}
