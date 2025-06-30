package dev.ag6.mclauncher.content;

import dev.ag6.mclauncher.MCLauncher;
import javafx.scene.Parent;
import lombok.NonNull;

public interface HasView {
    default void initialise(MCLauncher inst) {}

    @NonNull Parent getView();
}
