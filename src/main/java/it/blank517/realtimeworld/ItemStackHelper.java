package it.blank517.realtimeworld;

import org.bukkit.DyeColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;

import java.util.List;

class ItemStackHelper {

    @SuppressWarnings("SameParameterValue")
    static ItemStack createWool(DyeColor color, int amount, String name, List<String> description) {
        ItemStack item = new Wool(color).toItemStack(amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(description);
        item.setItemMeta(meta);
        return item;
    }
}
