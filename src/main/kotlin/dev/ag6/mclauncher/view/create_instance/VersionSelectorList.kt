package dev.ag6.mclauncher.view.create_instance

import dev.ag6.mclauncher.minecraft.MinecraftVersion
import dev.ag6.mclauncher.minecraft.MinecraftVersionHandler
import dev.ag6.mclauncher.minecraft.Type
import io.github.palexdev.materialfx.utils.FXCollectors
import javafx.scene.control.ListView
import javafx.scene.control.Tab
import javafx.scene.control.TabPane

class VersionSelectorList : TabPane() {
    init {
        val releaseTab = VersionTab("Release", Type.RELEASE)
        val snapshotTab = VersionTab("Snapshot", Type.SNAPSHOT)


        tabClosingPolicy = TabClosingPolicy.UNAVAILABLE
        this.tabs.addAll(releaseTab, snapshotTab)
    }

    fun getSelectedVersion(): MinecraftVersion {
        val selectedTab = this.selectionModel.selectedItem as VersionTab
        return selectedTab.listView.selectionModel.selectedItem
    }

    class VersionTab(title: String, versionType: Type) : Tab(title) {
        val listView: ListView<MinecraftVersion> = ListView()

        init {
            listView.items = MinecraftVersionHandler.minecraftVersions.stream().filter { it.type == versionType }
                .collect(FXCollectors.toList())
            listView.selectionModel.selectFirst()

            this.content = listView
        }
    }
}