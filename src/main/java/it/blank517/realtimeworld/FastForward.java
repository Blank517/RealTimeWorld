package it.blank517.realtimeworld;

import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Objects;

class FastForward implements Runnable {

    private final RealTimeWorld plugin;
    private final List<String> worlds;
    private final long tick;
    private final int delay;
    private final int period;

    FastForward(RealTimeWorld plugin, List<String> worlds, long tick, int delay) {
        this.plugin = plugin;
        this.worlds = worlds;
        this.tick = tick;
        this.delay = delay;
        this.period = 20;
    }

    public void run() {
        Task task = plugin.getTask();
        BukkitRunnable runnable = task.getRunnable();
        plugin.getMethods().whitelistDaylightCycle(false);
        for (String world : worlds) {
            World currentWorld = Objects.requireNonNull(plugin.getServer().getWorld(world));
            do {
                currentWorld.setTime(currentWorld.getTime() + 1);
            } while (currentWorld.getTime() != tick);
        }
        runnable.runTaskTimerAsynchronously(plugin, delay, period);
        task.setId(runnable.getTaskId());
    }
}
