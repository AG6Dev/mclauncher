package dev.ag6.mclauncher.instance;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class GameInstance {
    public final StringProperty name;
    public final StringProperty description;

    public GameInstance(String name, String description) {
        this.name = new SimpleStringProperty(name);
        this.description = new SimpleStringProperty(description);
    }
}
