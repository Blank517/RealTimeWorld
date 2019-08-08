package it.blank517.realtimeworld;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

class Task {

    private final RealTimeWorld plugin;
    private BukkitRunnable task;
    private int id = -1;

    Task(RealTimeWorld plugin) {
        this.plugin = plugin;
    }

    BukkitRunnable getRunnable() {
        return task;
    }

    private void initializeTask() {
        task = new BukkitRunnable() {
            @Override
            public void run() {
                plugin.getMethods().convert();
            }
        };
    }

    void startTask(List<String> worlds) {
        if (isRunning()) {
            stopTask();
        }
        initializeTask();
        plugin.getMethods().setWorldsDaylightCycle();
        new Thread(new FastForward(plugin, worlds, plugin.getMethods().realTimeToTickTime(), 0)).start();
    }

    void stopTask() {
        task.cancel();
        id = -1;
        plugin.getMethods().whitelistDaylightCycle(true);
    }

    void setId(int runnableId) {
        id = runnableId;
    }

    Boolean isRunning() {
        if (id == -1) {
            return false;
        }
        return Bukkit.getScheduler().isCurrentlyRunning(task.getTaskId());
    }
}
