package it.blank517.realtimeworld;

import org.bukkit.GameRule;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

class RealTimeWorld {

    private final Main plugin;
    private BukkitRunnable task = null;

    RealTimeWorld(Main plugin) {
        this.plugin = plugin;
    }

    private void InitializeTask() {
        task = new BukkitRunnable() {
            @Override
            public void run() {
                Convert();
            }
        };
    }

    void StartTask(List<String> worlds, int delay) {
        if (task != null) {
            task.cancel();
        }
        InitializeTask();
        Main.config.setWorldsDaylightCycle();
        new Thread(new FastForward(plugin, worlds, RT2TT(), delay)).start();
    }

    void StopTask() {
        task.cancel();
        DoDaylightCycle(true);
    }

    BukkitRunnable getTask() {
        return task;
    }

    private void Convert() {
        long tickTime = RT2TT();
        for (String world : Main.config.whitelist) {
            Objects.requireNonNull(plugin.getServer().getWorld(world)).setTime(tickTime);
        }
    }

    private long RT2TT() {
        ZonedDateTime now = ZonedDateTime.now();
        double hour = now.getHour();
        double minute = now.getMinute();
        double second = now.getSecond();
        double offset = hour < 12 ? 18000 : -6000;
        double tickHour = hour * 1000 + offset;
        double tickMinute = 1000 / (60 / minute);
        double tickSecond = (1000D / 60D / 60D) * second;
        return Math.round(tickHour + tickMinute + tickSecond);
    }

    void DoDaylightCycle(Boolean value) {
        for (String world : Main.config.whitelist) {
            Objects.requireNonNull(plugin.getServer().getWorld(world)).setGameRule(GameRule.DO_DAYLIGHT_CYCLE, value);
        }
    }

}
