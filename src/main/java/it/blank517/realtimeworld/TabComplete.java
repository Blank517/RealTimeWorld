package it.blank517.realtimeworld;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


class TabComplete implements TabCompleter {

    private final RealTimeWorld plugin;

    private final ArrayList<String> Hints_1 = new ArrayList<>(Arrays.asList("disable", "enable", "gui", "timezone", "whitelist"));
    private final ArrayList<String> Hints_2_whitelist = new ArrayList<>(Arrays.asList("add", "list", "remove"));
    private final ArrayList<String> Hints_2_timezone = new ArrayList<>(Arrays.asList("get", "set"));

    TabComplete(RealTimeWorld plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                      @NotNull String alias, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("realtimeworld")) {
            final int argsLength = args.length;
            ArrayList<String> Hints = new ArrayList<>();
            if (argsLength == 1) {
                Hints.addAll(Hints_1);
            } else if (argsLength == 2) {
                if (args[0].equalsIgnoreCase("timezone")) {
                    Hints.addAll(Hints_2_timezone);
                } else if (args[0].equalsIgnoreCase("whitelist")) {
                    Hints.addAll(Hints_2_whitelist);
                }
            } else if (argsLength == 3) {
                if (args[0].equalsIgnoreCase("timezone")) {
                    if (args[1].equalsIgnoreCase("set")) {
                        TimeZoneAndOffSet tzo = new TimeZoneAndOffSet();
                        Hints.addAll(tzo.genTimeZoneList());
                    }
                } else if (args[0].equalsIgnoreCase("whitelist")) {
                    if (args[1].equalsIgnoreCase("add")) {
                        List<World> worlds = plugin.getServer().getWorlds();
                        List<String> whitelist = plugin.getCustomConfig().get().getStringList("Whitelist");
                        for (World world : worlds) {
                            if (!whitelist.contains(world.getName())) {
                                Hints.add(world.getName());
                            }
                        }
                    } else if (args[1].equalsIgnoreCase("remove")) {
                        List<String> whitelist = plugin.getCustomConfig().get().getStringList("Whitelist");
                        Hints.addAll(whitelist);
                    }
                }
            }
            if (Hints.isEmpty()) {
                return null;
            }
            final List<String> completions = new ArrayList<>();
            // Copy matches of first argument from list (ex: if first arg is 'e' will return just 'enable')
            StringUtil.copyPartialMatches(args[argsLength - 1], Hints, completions);
            Collections.sort(completions);
            return completions;
        }
        return null;
    }
}