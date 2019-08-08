package it.blank517.realtimeworld;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class GUIManager implements InventoryHolder {

    private final Inventory inv;

    @SuppressWarnings("SameParameterValue")
    GUIManager(String title, int size) {
        this.inv = Bukkit.createInventory(this, size, title);
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inv;
    }

    void setItem(int pos, ItemStack item) {
        inv.setItem(pos, item);
    }

    void openInventory(Player p) {
        p.openInventory(inv);
    }

}
