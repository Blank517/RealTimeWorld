package it.blank517.realtimeworld;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

class Commands implements CommandExecutor {

    private final RealTimeWorld plugin;
    private int argsLength;

    Commands(RealTimeWorld plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command,
                             @NotNull String alias, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("realtimeworld")) {
            Messages messages = plugin.getMessages();
            if (sender instanceof Player && !sender.hasPermission("realtimeworld.cmd")) {
                sender.sendMessage(messages.get(-1));
                return false;
            }
            Config config = plugin.getCustomConfig();
            Task task = plugin.getTask();
            argsLength = args.length;
            if (argsLength == 0 || argsLength > 3) {
                sender.sendMessage(messages.get(0, new String[]{alias}));
                return true;
            }
            switch (args[0].toLowerCase()) {
                case "enable": {
                    cmdEnable(sender, alias, config, task, messages);
                    break;
                }
                case "disable": {
                    cmdDisable(sender, alias, config, task, messages);
                    break;
                }
                case "gui": {
                    cmdGui(sender, alias, messages);
                    break;
                }
                case "timezone":
                    cmdTimezone(sender, alias, args, config, messages);
                    break;
                case "whitelist":
                    cmdWhitelist(sender, alias, args, config, task, messages);
                    break;
                default: {
                    // Help message
                    sender.sendMessage(messages.get(0, new String[]{alias}));
                    break;
                }
            }
            return true;
        }
        return false;
    }

    private void cmdEnable(CommandSender sender, String alias, Config config, Task task, Messages messages) {
        if (argsLength == 1) {
            if (!config.isEnabled()) {
                config.set("Enabled", true);
                config.save();
                config.setEnabled(true);
                task.startTask();
                sender.sendMessage(messages.get(1)); // Time synchronization enabled
            } else {
                sender.sendMessage(messages.get(2)); // Time synchronization is already enabled
            }
        } else {
            sender.sendMessage(messages.get(0, new String[]{alias}));
        }
    }

    private void cmdDisable(CommandSender sender, String alias, Config config, Task task, Messages messages) {
        if (argsLength == 1) {
            if (config.isEnabled()) {
                config.set("Enabled", false);
                config.save();
                config.setEnabled(false);
                task.stopTask();
                sender.sendMessage(messages.get(3)); // Time synchronization disabled
            } else {
                sender.sendMessage(messages.get(4)); // Time synchronization is already disabled
            }
        } else {
            sender.sendMessage(messages.get(0, new String[]{alias}));
        }
    }

    private void cmdGui(CommandSender sender, String alias, Messages messages) {
        if (argsLength == 1) {
            if (sender instanceof Player) {
                plugin.getInvWorldsList().load();
                plugin.getInvWorldsList().getGUIManager().openInventory((Player) sender);
            } else {
                sender.sendMessage(messages.get(8)); // You must be a player to use this command
            }
        } else {
            sender.sendMessage(messages.get(0, new String[]{alias}));
        }
    }

    private void cmdTimezone(CommandSender sender, String alias, String[] args, Config config, Messages messages) {
        if (argsLength == 2 && args[1].equalsIgnoreCase("get")) {
            sender.sendMessage(messages.get(0, new String[]{alias}));
        } else if (argsLength == 3 && args[1].equalsIgnoreCase("set")) {
            if (config.setTimezone(args[2])) {
                config.save();
                sender.sendMessage(messages.get(10, new String[]{config.getTimezone().toString()}));
            } else {
                sender.sendMessage(messages.get(11, new String[]{args[2]}));
            }
        } else {
            sender.sendMessage(messages.get(0, new String[]{alias}));
        }
    }

    private void cmdWhitelist(CommandSender sender, String alias, String[] args, Config config, Task task, Messages messages) {
        if (argsLength == 2 && args[1].equalsIgnoreCase("list")) {
            sender.sendMessage(messages.get(5)); // Worlds Whitelist: - ...
            return;
        } else if (argsLength == 3) {
            if (args[1].equalsIgnoreCase("add")) {
                List<String> whitelist = config.get().getStringList("Whitelist");
                whitelist.add(args[2]);
                List<String> newWhitelist = whitelist.stream()
                        .distinct()
                        .collect(Collectors.toList());
                config.set("Whitelist", newWhitelist);
                config.save();
                if (task.isRunning()) {
                    task.stopTask();
                }
                sender.sendMessage(messages.get(6, new String[]{args[2]})); // Added to whitelist: '...'
                if (config.isEnabled()) {
                    task.startTask();
                }
                return;
            } else if (args[1].equalsIgnoreCase("remove")) {
                List<String> whitelist = config.get().getStringList("Whitelist");
                whitelist.remove(args[2]);
                config.set("Whitelist", whitelist);
                config.save();
                plugin.getMethods().setWorldsDaylightCycle();
                sender.sendMessage(messages.get(7, new String[]{args[2]})); // Removed from whitelist: '...'
                return;
            }
        }
        sender.sendMessage(messages.get(0, new String[]{alias}));
    }

}
