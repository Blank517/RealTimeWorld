package it.blank517.realtimeworld;

import org.bukkit.scheduler.BukkitRunnable;

class Task {

    private final RealTimeWorld plugin;
    private BukkitRunnable task;
    private int id = -1;

    Task(RealTimeWorld plugin) {
        this.plugin = plugin;
    }

    private void initializeTask() {
        task = new BukkitRunnable() {
            @Override
            public void run() {
                plugin.getMethods().convert();
            }
        };
    }

    void startTask() {
        if (isRunning()) {
            stopTask();
        }
        initializeTask();
        plugin.getMethods().setWorldsDaylightCycle();
        plugin.getMethods().whitelistDaylightCycle(false);
        task.runTaskTimerAsynchronously(plugin, 0, 20);
        id = task.getTaskId();
    }

    void stopTask() {
        task.cancel();
        id = -1;
        plugin.getMethods().whitelistDaylightCycle(true);
    }

    Boolean isRunning() {
        return id != -1;
    }
}
