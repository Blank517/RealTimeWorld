package it.blank517.realtimeworld;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
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

    private HashMap<Integer, ArrayList<String>> loadHints() {
        ArrayList<String> Hints_1 = new ArrayList<>();
        Hints_1.add("disable");
        Hints_1.add("enable");
        Hints_1.add("gui");
        Hints_1.add("whitelist");

        ArrayList<String> Hints_2 = new ArrayList<>();
        Hints_2.add("add");
        Hints_2.add("list");
        Hints_2.add("remove");

        HashMap<Integer, ArrayList<String>> Hints = new HashMap<>();
        Hints.put(1, Hints_1);
        Hints.put(2, Hints_2);

        return Hints;
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
                    task.startTask(config.getWhitelist());
                }
            };
            runnable.runTaskLater(this, 1L);
        } else {
            getLogger().info(messages.get(3));
            methods.setWorldsDaylightCycle();
        }

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new InventoryClick(this), plugin);

        Objects.requireNonNull(getCommand("realtimeworld")).setTabCompleter(new TabComplete(this, loadHints()));
        Objects.requireNonNull(getCommand("realtimeworld")).setExecutor(new Commands(this));
    }
}
