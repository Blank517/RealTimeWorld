package it.blank517.realtimeworld;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Commands implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String alias, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("realtimeworld")) {
            if (sender instanceof Player && !sender.hasPermission("realtimeworld.cmd")) {
                return false;
            }
            Arrays.setAll(args, i -> args[i].toLowerCase());
            String message = ChatColor.GOLD + "=============" + ChatColor.YELLOW + " RealTimeWorld " + ChatColor.GOLD + "=============\n";
            switch (args.length) {
                case 1:
                    if (args[0].equals("enable")) {
                        Main.config.set("Enabled", true);
                        Main.config.save();
                        Main.internalClock.StartTask(Main.config.whitelist, 0);
                        message += ChatColor.GOLD + "| " + ChatColor.GREEN + "Time synchronization enabled\n" +
                                ChatColor.GOLD + "=======================================";
                        sender.sendMessage(message);
                        return true;
                    } else if (args[0].equals("disable")) {
                        Main.config.set("Enabled", false);
                        Main.config.save();
                        Main.internalClock.StopTask();
                        message += ChatColor.GOLD + "| " + ChatColor.RED + "Time synchronization disabled\n" +
                                ChatColor.GOLD + "=======================================";
                        sender.sendMessage(message);
                        return true;
                    }
                    break;
                case 2:
                    if (args[0].equals("whitelist") && args[1].equals("list")) {
                        List<String> whitelist = Main.config.get().getStringList("Whitelist");
                        StringBuilder sbMessage = new StringBuilder(message)
                                .append("| Worlds Whitelist:\n");
                        for (String world : whitelist) {
                            sbMessage.append(ChatColor.GOLD).append("|").append(ChatColor.WHITE).append(" - ").append(world).append("\n");
                        }
                        sbMessage.append(ChatColor.GOLD).append("=======================================");
                        sender.sendMessage(sbMessage.toString());
                        return true;
                    }
                    break;
                case 3:
                    if (args[0].equals("whitelist")) {
                        switch (args[1]) {
                            case "add": {
                                List<String> world = new ArrayList<>();
                                world.add(args[2]);
                                List<String> whitelist = Main.config.get().getStringList("Whitelist");
                                whitelist.add(args[2]);
                                Main.config.set("Whitelist", whitelist);
                                Main.config.save();
                                Main.internalClock.StopTask();
                                message += ChatColor.GOLD + "| " + ChatColor.WHITE + "Added to whitelist: '" + args[2] + "'\n" +
                                        ChatColor.GOLD + "=======================================";
                                sender.sendMessage(message);
                                Main.internalClock.StartTask(world, 0);
                                return true;
                            }
                            case "remove": {
                                List<String> whitelist = Main.config.get().getStringList("Whitelist");
                                whitelist.remove(args[2]);
                                Main.config.set("Whitelist", whitelist);
                                Main.config.save();
                                Main.config.setWorldsDaylightCycle();
                                message += ChatColor.GOLD + "| " + ChatColor.WHITE + "Removed from whitelist: '" + args[2] + "'\n" +
                                        ChatColor.GOLD + "=======================================";
                                sender.sendMessage(message);
                                return true;
                            }
                        }
                    }
                    break;
            }
            message += "| " + ChatColor.YELLOW + "/" + alias + ChatColor.AQUA + " enable" + ChatColor.WHITE + " - Enable the daylight synchronization\n" +
                    ChatColor.GOLD + "| " + ChatColor.YELLOW + "/" + alias + ChatColor.AQUA + " disable" + ChatColor.WHITE + " - Disable the daylight synchronization\n" +
                    ChatColor.GOLD + "| " + ChatColor.YELLOW + "/" + alias + ChatColor.AQUA + " whitelist list\n" +
                    ChatColor.GOLD + "| " + ChatColor.YELLOW + "/" + alias + ChatColor.AQUA + " whitelist add " + ChatColor.GREEN + "<world>\n" +
                    ChatColor.GOLD + "| " + ChatColor.YELLOW + "/" + alias + ChatColor.AQUA + " whitelist remove " + ChatColor.GREEN + "<world>\n" +
                    ChatColor.GOLD + "=======================================";
            sender.sendMessage(message);
        }
        return false;
    }

}
