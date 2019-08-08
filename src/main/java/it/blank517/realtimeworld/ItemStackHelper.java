package it.blank517.realtimeworld;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

class ItemStackHelper {

    @SuppressWarnings("SameParameterValue")
    static ItemStack createItem(Material material, int amount, String name, List<String> description) {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(description);
        item.setItemMeta(meta);
        return item;
    }
}
