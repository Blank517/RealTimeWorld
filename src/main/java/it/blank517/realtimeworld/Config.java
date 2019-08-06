package it.blank517.realtimeworld;

import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

class Config {

    private final Main plugin;
    List<String> whitelist;
    private FileConfiguration config;

    Config(Main plugin) {
        this.plugin = plugin;
    }

    void load(Path path) {
        config = YamlConfiguration.loadConfiguration(path.toFile());
        if (config.saveToString().equals("")) {
            plugin.getLogger().warning(path + " is misconfigured");
        }
    }

    void save() {
        try {
            get().save(Paths.get(plugin.getDataFolder().toString(), "config.yml").toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        load(Paths.get(plugin.getDataFolder().toString(), "config.yml"));
    }

    FileConfiguration get() {
        return config;
    }

    void set(@NotNull String key, @Nullable Object value) {
        get().set(key, value);
    }

    void setWorldsDaylightCycle() {
        List<World> worlds = plugin.getServer().getWorlds();
        whitelist = get().getStringList("Whitelist");
        for (World world : worlds) {
            if (whitelist.contains(world.getName())) {
                world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            } else {
                world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
            }
        }
    }
}
