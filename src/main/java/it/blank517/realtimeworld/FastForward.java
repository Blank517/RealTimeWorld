package it.blank517.realtimeworld;

import org.bukkit.World;

import java.util.List;
import java.util.Objects;

class FastForward implements Runnable {

    private final Main plugin;
    private final List<String> worlds;
    private final long tick;
    private final int delay;
    private final int period;

    FastForward(Main plugin, List<String> worlds, long tick, int delay) {
        this.plugin = plugin;
        this.worlds = worlds;
        this.tick = tick;
        this.delay = delay;
        this.period = 20;
    }

    public void run() {
        for (String world : worlds) {
            World currentWorld = Objects.requireNonNull(plugin.getServer().getWorld(world));
            do {
                currentWorld.setTime(currentWorld.getTime() + 1);
            } while (currentWorld.getTime() != tick);
            Main.internalClock.DoDaylightCycle(false);
        }
        Main.internalClock.getTask().runTaskTimerAsynchronously(plugin, delay, period);
    }
}
