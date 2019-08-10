package it.blank517.realtimeworld;

import org.bukkit.World;

import java.time.Instant;
import java.time.ZoneId;
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

    private long realTimeToTickTime() {
        ZoneId timezone = plugin.getCustomConfig().getTimezone();
        Instant instant = Instant.now();
        ZonedDateTime now = ZonedDateTime.ofInstant(instant, timezone);
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
            Objects.requireNonNull(plugin.getServer().getWorld(world)).setGameRuleValue("doDaylightCycle", value.toString());
        }
    }

    void setWorldsDaylightCycle() {
        List<World> worlds = plugin.getServer().getWorlds();
        List<String> whitelist = plugin.getCustomConfig().getWhitelist();
        if (!plugin.getCustomConfig().isEnabled()) {
            whitelist.clear();
        }
        for (World world : worlds) {
            if (whitelist.contains(world.getName())) {
                world.setGameRuleValue("doDaylightCycle", "false");
            } else {
                world.setGameRuleValue("doDaylightCycle", "true");
            }
        }
    }
}
