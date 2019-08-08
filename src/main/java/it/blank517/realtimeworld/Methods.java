package it.blank517.realtimeworld;

import org.bukkit.GameRule;
import org.bukkit.World;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

class Methods {

    private final RealTimeWorld plugin;

    Methods(RealTimeWorld plugin) {
        this.plugin = plugin;
    }

    void convert() {
        long tickTime = realTimeToTickTime();
        for (String world : plugin.getCustomConfig().getWhitelist()) {
            Objects.requireNonNull(RealTimeWorld.getInstance().getServer().getWorld(world)).setTime(tickTime);
        }
    }

    long realTimeToTickTime() {
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

    void whitelistDaylightCycle(Boolean value) {
        for (String world : plugin.getCustomConfig().getWhitelist()) {
            Objects.requireNonNull(plugin.getServer().getWorld(world)).setGameRule(GameRule.DO_DAYLIGHT_CYCLE, value);
        }
    }

    void setWorldsDaylightCycle() {
        List<World> worlds = plugin.getServer().getWorlds();
        List<String> whitelist = plugin.getCustomConfig().getWhitelist();
        for (World world : worlds) {
            if (whitelist.contains(world.getName())) {
                world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            } else {
                world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
            }
        }
    }
}
