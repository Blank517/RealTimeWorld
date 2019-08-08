package it.blank517.realtimeworld;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class InventoryClick implements Listener {

    private final RealTimeWorld plugin;

    InventoryClick(RealTimeWorld plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void event(InventoryClickEvent event) {
        Inventory inv = event.getClickedInventory();
        InventoryHolder holder = Objects.requireNonNull(inv).getHolder();
        if (holder != plugin.getInvWorldsList().getGUIManager().getInventory().getHolder()) {
            return;
        }
        if (event.getClick().equals(ClickType.NUMBER_KEY)) {
            event.setCancelled(true);
        }
        event.setCancelled(true);
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        String clickedItemName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());
        if (plugin.getCustomConfig().getWhitelist().contains(clickedItemName)) {
            player.performCommand("realtimeworld whitelist remove " + clickedItemName);
        } else {
            player.performCommand("realtimeworld whitelist add " + clickedItemName);
        }
        plugin.getInvWorldsList().load();
    }
}

