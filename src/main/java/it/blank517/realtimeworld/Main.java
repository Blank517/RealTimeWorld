package it.blank517.realtimeworld;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Main extends JavaPlugin {

    static RealTimeWorld internalClock;
    static Config config;

    @Override
    public void onEnable() {

        if (!(new File(getDataFolder(), "config.yml")).exists()) {
            this.saveResource("config.yml", false);
        }

        config = new Config(this);
        config.load(Paths.get(this.getDataFolder().toString(), "config.yml"));
        config.setWorldsDaylightCycle();

        internalClock = new RealTimeWorld(this);
        if (config.get().getBoolean("Enabled")) {
            internalClock.StartTask(config.whitelist, 200);
        }

        ArrayList<String> HINTS_1 = new ArrayList<>();
        HINTS_1.add("whitelist");
        HINTS_1.add("disable");
        HINTS_1.add("enable");

        ArrayList<String> HINTS_2 = new ArrayList<>();
        HINTS_2.add("add");
        HINTS_2.add("list");
        HINTS_2.add("remove");

        HashMap<Integer, ArrayList<String>> HINTS = new HashMap<>();
        HINTS.put(1, HINTS_1);
        HINTS.put(2, HINTS_2);

        Objects.requireNonNull(getCommand("realtimeworld")).setTabCompleter(new TabCompleteCmds(this, HINTS));
        Objects.requireNonNull(getCommand("realtimeworld")).setExecutor(new Commands());
    }

    @Override
    public void onDisable() {

    }

}
