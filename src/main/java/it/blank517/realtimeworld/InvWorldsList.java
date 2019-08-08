package it.blank517.realtimeworld;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.Collections;
import java.util.List;

class InvWorldsList {

    private final RealTimeWorld plugin;
    private final GUIManager guiManager;

    InvWorldsList(RealTimeWorld plugin, GUIManager guiManager) {
        this.plugin = plugin;
        this.guiManager = guiManager;
    }

    GUIManager getGUIManager() {
        return guiManager;
    }

    void load() {
        List<World> worlds = plugin.getServer().getWorlds();
        List<String> whitelist = plugin.getCustomConfig().getWhitelist();
        String worldName;
        for (int i = 0; i < worlds.size(); i++) {
            worldName = worlds.get(i).getName();
            if (whitelist.contains(worldName)) {
                guiManager.setItem(i, ItemStackHelper.createItem(Material.GREEN_WOOL, 1, ChatColor.RESET + worldName,
                        Collections.singletonList(ChatColor.GREEN + "Sync: enabled")));
            } else {
                guiManager.setItem(i, ItemStackHelper.createItem(Material.RED_WOOL, 1, ChatColor.RESET + worldName,
                        Collections.singletonList(ChatColor.RED + "Sync: disabled")));
            }
        }
    }
}