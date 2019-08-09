package it.blank517.realtimeworld;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DateTimeException;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

class Config {

    private final RealTimeWorld plugin;
    private FileConfiguration config;
    private List<String> whitelist;
    private Boolean enabled;
    private ZoneId timezone;

    Config(RealTimeWorld plugin) {
        this.plugin = plugin;
    }

    void load(Path path) {
        config = YamlConfiguration.loadConfiguration(path.toFile());
        if (config.saveToString().equals("")) {
            plugin.getLogger().warning(path + " is misconfigured");
        }
        validate();
        enabled = config.getBoolean("Enabled");
        whitelist = config.getStringList("Whitelist");

        setTimezone(Objects.requireNonNull(config.getString("Timezone")));
    }

    private void validate() {
        String[] keys = {"Enabled", "Timezone", "Whitelist"};
        boolean edited = false;
        for (String key : keys) {
            if (!config.contains(key)) {
                plugin.getLogger().warning("Config key '" + key + "' is missing");
                edited = true;
                config.createSection(key);
                Object value;
                switch (key) {
                    case "Enabled": {
                        value = true;
                        break;
                    }
                    case "Timezone": {
                        value = ZoneId.systemDefault().toString();
                        break;
                    }
                    case "Whitelist": {
                        value = Collections.singletonList("world");
                        break;
                    }
                    default: {
                        value = "PLUGIN_ERROR";
                        break;
                    }
                }
                config.set(key, value);
                plugin.getLogger().warning("Assigned default value '" + value + "'");
            }
        }
        if (edited) {
            save();
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
        config.set(key, value);
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

    ZoneId getTimezone() {
        return timezone;
    }

    Boolean setTimezone(String zoneIdAndOffset) {
        try {
            String[] zoneId = zoneIdAndOffset.split("-->");
            timezone = ZoneId.of(zoneId[0]);
            set("Timezone", timezone.toString());
            return true;
        } catch (DateTimeException ignore) {
            return false;
        }
    }

}
