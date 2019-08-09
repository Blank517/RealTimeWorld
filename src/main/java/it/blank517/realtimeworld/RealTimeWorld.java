package it.blank517.realtimeworld;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.nio.file.Paths;
import java.util.Objects;

public class RealTimeWorld extends JavaPlugin {

    private static RealTimeWorld plugin;
    private Config config;
    private Task task;
    private Methods methods;
    private Messages messages;
    private InvWorldsList invWorldsList;

    static RealTimeWorld getInstance() {
        return plugin;
    }

    Config getCustomConfig() {
        return config;
    }

    Task getTask() {
        return task;
    }

    Methods getMethods() {
        return methods;
    }

    Messages getMessages() {
        return messages;
    }

    InvWorldsList getInvWorldsList() {
        return invWorldsList;
    }

    @Override
    public void onEnable() {
        plugin = this;
        if (!(new File(getDataFolder(), "config.yml")).exists()) {
            this.saveResource("config.yml", false);
        }

        UpdateCheck
                .of(this)
                .resourceId(70124)
                .handleResponse((versionResponse, version) -> {
                    switch (versionResponse) {
                        case FOUND_NEW:
                            getLogger().warning("New version of the plugin was found: " + version);
                            break;
                        case LATEST:
                            getLogger().info("You are on the latest version of the plugin.");
                            break;
                        case UNAVAILABLE:
                            getLogger().warning("Unable to perform an update check.");
                            break;
                        default:
                            getLogger().warning("There is a problem in the Update Checker.");
                            break;
                    }
                }).check();

        methods = new Methods(this);
        messages = new Messages(this);
        config = new Config(this);
        invWorldsList = new InvWorldsList(this, new GUIManager("RealTimeWorld", 54));

        config.load(Paths.get(this.getDataFolder().toString(), "config.yml"));
        methods.setWorldsDaylightCycle();

        task = new Task(this);

        if (config.isEnabled()) {
            getLogger().info(messages.get(1));
            BukkitRunnable runnable = new BukkitRunnable() {
                @Override
                public void run() {
                    task.startTask();
                }
            };
            runnable.runTaskLater(this, 1L);
        } else {
            getLogger().info(messages.get(3));
            methods.setWorldsDaylightCycle();
        }

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new InventoryClick(this), plugin);

        Objects.requireNonNull(getCommand("realtimeworld")).setTabCompleter(new TabComplete(this));
        Objects.requireNonNull(getCommand("realtimeworld")).setExecutor(new Commands(this));
    }
}
