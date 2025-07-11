module dev.ag6.mclauncher {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;
    requires kotlinx.coroutines.core.jvm;
    requires MaterialFX;
    requires com.google.gson;
    requires fr.brouillard.oss.cssfx;
    requires gson.kotlin.adapter;
    requires kotlinx.serialization.core.jvm;
    requires kotlinx.serialization.json.jvm;

    opens dev.ag6.mclauncher to javafx.fxml;
    exports dev.ag6.mclauncher;
}