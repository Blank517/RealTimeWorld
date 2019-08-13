package it.blank517.realtimeworld;

import org.bstats.bukkit.Metrics;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
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

        setupMetrics(this);

        ConsoleCommandSender console = this.getServer().getConsoleSender();

        if (!(new File(getDataFolder(), "config.yml")).exists()) {
            this.saveResource("config.yml", false);
        }

        UpdateCheck
                .of(this)
                .resourceId(70124)
                .handleResponse((versionResponse, version) -> {
                    String prefix = "[" + getDescription().getPrefix() + "] ";
                    switch (versionResponse) {
                        case FOUND_NEW:
                            console.sendMessage(ChatColor.GOLD + prefix + "New version of the plugin was found: " + version);
                            break;
                        case LATEST:
                            console.sendMessage(ChatColor.GREEN + prefix + "You are running the latest version.");
                            break;
                        case UNAVAILABLE:
                            console.sendMessage(ChatColor.GOLD + prefix + "Unable to perform an update check.");
                            break;
                        default:
                            console.sendMessage(ChatColor.RED + prefix + "There is a problem in the Update Checker.");
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
            console.sendMessage(messages.get(1));
            BukkitRunnable runnable = new BukkitRunnable() {
                @Override
                public void run() {
                    task.startTask();
                }
            };
            runnable.runTaskLater(this, 1L);
        } else {
            console.sendMessage(messages.get(3));
            methods.setWorldsDaylightCycle();
        }

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new InventoryClick(this), plugin);

        Objects.requireNonNull(getCommand("realtimeworld")).setTabCompleter(new TabComplete(this));
        Objects.requireNonNull(getCommand("realtimeworld")).setExecutor(new Commands(this));
    }

    private void setupMetrics(RealTimeWorld plugin) {
        Metrics metrics = new Metrics(plugin);
    }
}
