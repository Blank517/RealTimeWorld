package it.blank517.realtimeworld;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
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
        List<String> whitelist = plugin.getCustomConfig().get().getStringList("Whitelist");
        if (plugin.getCustomConfig().isEnabled()) {
            guiManager.setItem(53, ItemStackHelper.createWool(DyeColor.GREEN, 1, ChatColor.RESET + "Plugin",
                    Collections.singletonList(ChatColor.GREEN + "Enabled")));
        } else {
            guiManager.setItem(53, ItemStackHelper.createWool(DyeColor.RED, 1, ChatColor.RESET + "Plugin",
                    Collections.singletonList(ChatColor.RED + "Disabled")));
        }
        for (int i = 0; i < worlds.size(); i++) {
            String worldName = worlds.get(i).getName();
            if (whitelist.contains(worldName)) {
                guiManager.setItem(i, ItemStackHelper.createWool(DyeColor.GREEN, 1, ChatColor.RESET + worldName,
                        Collections.singletonList(ChatColor.GREEN + "Sync: enabled")));
            } else {
                guiManager.setItem(i, ItemStackHelper.createWool(DyeColor.RED, 1, ChatColor.RESET + worldName,
                        Collections.singletonList(ChatColor.RED + "Sync: disabled")));
            }
        }
    }
}
