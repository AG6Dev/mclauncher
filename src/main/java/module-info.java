module dev.ag6.mclauncher {
    requires kotlin.stdlib;
    requires kotlinx.coroutines.core.jvm;
    requires MaterialFX;
    requires com.google.gson;
    requires fr.brouillard.oss.cssfx;
    requires gson.kotlin.adapter;

    opens dev.ag6.mclauncher to javafx.fxml;
    exports dev.ag6.mclauncher;
}