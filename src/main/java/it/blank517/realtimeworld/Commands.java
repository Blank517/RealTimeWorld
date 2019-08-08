package it.blank517.realtimeworld;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class Commands implements CommandExecutor {

    private final RealTimeWorld plugin;

    Commands(RealTimeWorld plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String alias, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("realtimeworld")) {
            if (sender instanceof Player && !sender.hasPermission("realtimeworld.cmd")) {
                return false;
            }
            Arrays.setAll(args, i -> args[i].toLowerCase());
            Messages messages = plugin.getMessages();
            Config config = plugin.getCustomConfig();
            Task task = plugin.getTask();
            int argsLength = args.length;
            switch (argsLength) {
                case 1:
                    if (args[0].equals("enable")) {
                        if (!config.isEnabled()) {
                            config.set("Enabled", true);
                            config.save();
                            config.setEnabled(true);
                            task.startTask(config.getWhitelist());
                            // Time synchronization enabled
                            sender.sendMessage(messages.get(1));
                        } else {
                            // Time synchronization is already enabled
                            sender.sendMessage(messages.get(2));
                        }
                        return true;
                    } else if (args[0].equals("disable")) {
                        if (config.isEnabled()) {
                            config.set("Enabled", false);
                            config.save();
                            config.setEnabled(false);
                            task.stopTask();
                            // Time synchronization disabled
                            sender.sendMessage(messages.get(3));
                        } else {
                            // Time synchronization is already disabled
                            sender.sendMessage(messages.get(4));
                        }
                        return true;
                    }
                    break;
                case 2:
                    if (args[0].equals("whitelist") && args[1].equals("list")) {
                        // Worlds Whitelist:
                        // - ...
                        sender.sendMessage(messages.get(5));
                        return true;
                    }
                    break;
                case 3:
                    if (args[0].equals("whitelist")) {
                        String firstArgument = args[1];
                        switch (firstArgument) {
                            case "add": {
                                List<String> whitelist = config.get().getStringList("Whitelist");
                                whitelist.add(args[2]);
                                whitelist = whitelist.stream()
                                        .distinct()
                                        .collect(Collectors.toList());
                                config.set("Whitelist", whitelist);
                                config.save();
                                if (task.isRunning()) {
                                    task.stopTask();
                                }
                                // Added to whitelist: '...'
                                sender.sendMessage(messages.get(6));
                                if (config.isEnabled()) {
                                    task.startTask(whitelist);
                                }
                                return true;
                            }
                            case "remove": {
                                List<String> whitelist = config.get().getStringList("Whitelist");
                                whitelist.remove(args[2]);
                                config.set("Whitelist", whitelist);
                                config.save();
                                plugin.getMethods().setWorldsDaylightCycle();
                                // Removed from whitelist: '...'
                                sender.sendMessage(messages.get(7));
                                return true;
                            }
                        }
                    }
                    break;
            }
            // Help message
            sender.sendMessage(messages.get(0, alias));
        }
        return false;
    }

}
