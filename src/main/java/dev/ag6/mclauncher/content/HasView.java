package dev.ag6.mclauncher.content;

import javafx.scene.Parent;
import lombok.NonNull;

public interface HasView {
    @NonNull Parent getView();
}
