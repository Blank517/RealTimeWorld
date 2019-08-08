package it.blank517.realtimeworld;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

class Config {

    private final RealTimeWorld plugin;
    private FileConfiguration config;
    private List<String> whitelist;
    private Boolean enabled;

    Config(RealTimeWorld plugin) {
        this.plugin = plugin;
    }

    void load(Path path) {
        config = YamlConfiguration.loadConfiguration(path.toFile());
        if (config.saveToString().equals("")) {
            plugin.getLogger().warning(path + " is misconfigured");
        }
        enabled = config.getBoolean("Enabled");
        whitelist = config.getStringList("Whitelist");
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

    List<String> getWhitelist() {
        return whitelist;
    }

    void setEnabled(Boolean value) {
        enabled = value;
    }

    Boolean isEnabled() {
        return enabled;
    }

    void set(@NotNull String key, @Nullable Object value) {
        get().set(key, value);
    }

}
