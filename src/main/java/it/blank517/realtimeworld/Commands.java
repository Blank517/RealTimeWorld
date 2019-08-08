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

    private void argsOne(CommandSender sender, String[] args, Config config, Task task, Messages messages) {
        switch (args[0]) {
            case "enable": {
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
                break;
            }
            case "disable": {
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
                break;
            }
            case "gui": {
                if (sender instanceof Player) {
                    plugin.getInvWorldsList().load();
                    plugin.getInvWorldsList().getGUIManager().openInventory((Player) sender);
                    break;
                } else {
                    // You must be a player to use this command
                    sender.sendMessage(messages.get(8));
                    // No return so it print the help message
                }
                break;
            }
            default: {
                break;
            }
        }
    }

    @SuppressWarnings("unused")
    private void argsTwo(CommandSender sender, String[] args, Config config, Task task, Messages messages) {
        if (args[0].equals("whitelist") && args[1].equals("list")) {
            // Worlds Whitelist:
            // - ...
            sender.sendMessage(messages.get(5));
        }
    }

    private void argsThree(CommandSender sender, String[] args, Config config, Task task, Messages messages) {
        if (args[0].equals("whitelist")) {
            switch (args[1]) {
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
                    sender.sendMessage(messages.get(6, args[2]));
                    if (config.isEnabled()) {
                        task.startTask(whitelist);
                    }
                    break;
                }
                case "remove": {
                    List<String> whitelist = config.get().getStringList("Whitelist");
                    whitelist.remove(args[2]);
                    config.set("Whitelist", whitelist);
                    config.save();
                    plugin.getMethods().setWorldsDaylightCycle();
                    // Removed from whitelist: '...'
                    sender.sendMessage(messages.get(7, args[2]));
                    break;
                }
                default: {
                    break;
                }
            }
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command,
                             @NotNull String alias, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("realtimeworld")) {
            if (sender instanceof Player && !sender.hasPermission("realtimeworld.cmd")) {
                return false;
            }
            Arrays.setAll(args, i -> args[i].toLowerCase());
            Messages messages = plugin.getMessages();
            Config config = plugin.getCustomConfig();
            Task task = plugin.getTask();
            switch (args.length) {
                case 1:
                    argsOne(sender, args, config, task, messages);
                    break;
                case 2:
                    argsTwo(sender, args, config, task, messages);
                    break;
                case 3:
                    argsThree(sender, args, config, task, messages);
                    break;
                default: {
                    // Help message
                    sender.sendMessage(messages.get(0, alias));
                    break;
                }
            }
        }
        return false;
    }

}
