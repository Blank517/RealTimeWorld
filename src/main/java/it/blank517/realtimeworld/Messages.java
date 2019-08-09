package it.blank517.realtimeworld;

import org.bukkit.ChatColor;

import java.util.List;

class Messages {

    private final RealTimeWorld plugin;

    Messages(RealTimeWorld plugin) {
        this.plugin = plugin;
    }

    String get(int type) {
        return get(type, new String[0]);
    }

    String get(int type, String[] args) {
        StringBuilder message = new StringBuilder("\n")
                .append(ChatColor.GOLD).append("===============").append(ChatColor.YELLOW).append(" RealTimeWorld ")
                .append(ChatColor.GOLD).append("===============\n").append(ChatColor.GOLD).append("| ");
        switch (type) {
            case -1: {
                message.append(ChatColor.RED).append("You can't use this command!");
                break;
            }
            case 0: {
                message
                        .append(ChatColor.YELLOW).append("/").append(args[0])
                        .append(ChatColor.AQUA).append(" enable")
                        .append(ChatColor.WHITE).append(" - Enable the daylight sync\n")
                        .append(ChatColor.GOLD)

                        .append("| ").append(ChatColor.YELLOW).append("/").append(args[0])
                        .append(ChatColor.AQUA).append(" disable")
                        .append(ChatColor.WHITE).append(" - Disable the daylight sync\n")
                        .append(ChatColor.GOLD)

                        .append("| ").append(ChatColor.YELLOW).append("/").append(args[0])
                        .append(ChatColor.AQUA).append(" gui")
                        .append(ChatColor.WHITE).append(" - Open an user frendly GUI\n")
                        .append(ChatColor.GOLD)

                        .append("| ").append(ChatColor.YELLOW).append("/").append(args[0])
                        .append(ChatColor.AQUA).append(" whitelist list\n")
                        .append(ChatColor.GOLD)

                        .append("| ").append(ChatColor.YELLOW).append("/").append(args[0])
                        .append(ChatColor.AQUA).append(" whitelist add ").append(ChatColor.GREEN).append("<world>\n")
                        .append(ChatColor.GOLD)

                        .append("| ").append(ChatColor.YELLOW).append("/").append(args[0])
                        .append(ChatColor.AQUA).append(" whitelist remove ").append(ChatColor.GREEN).append("<world>\n")
                        .append(ChatColor.GOLD)

                        .append("| ").append(ChatColor.YELLOW).append("/").append(args[0])
                        .append(ChatColor.AQUA).append(" timezone get")
                        .append(ChatColor.WHITE).append(" - Get the current timezone\n")
                        .append(ChatColor.GOLD)

                        .append("| ").append(ChatColor.YELLOW).append("/").append(args[0])
                        .append(ChatColor.AQUA).append(" timezone set ").append(ChatColor.GREEN).append("<timezone>")
                        .append(ChatColor.WHITE).append(" - Set the timezone")
                        .append(ChatColor.GOLD);
                
                break;
            }
            case 1: {
                message.append(ChatColor.GREEN).append("Time synchronization enabled");
                break;
            }
            case 2: {
                message.append(ChatColor.GREEN).append("Time synchronization is already enabled");
                break;
            }
            case 3: {
                message.append(ChatColor.RED).append("Time synchronization disabled");
                break;
            }
            case 4: {
                message.append(ChatColor.RED).append("Time synchronization is already disabled");
                break;
            }
            case 5: {
                List<String> whitelist = plugin.getCustomConfig().getWhitelist();
                message.append("Worlds Whitelist:");
                for (String world : whitelist) {
                    message.append("\n").append(ChatColor.GOLD).append("|").append(ChatColor.WHITE).append(" - ").append(world);
                }
                break;
            }
            case 6: {
                message.append(ChatColor.WHITE).append("Added to whitelist: '").append(args[0]).append("'");
                break;
            }
            case 7: {
                message.append(ChatColor.WHITE).append("Removed from whitelist: '").append(args[0]).append("'");
                break;
            }
            case 8: {
                message.append(ChatColor.RED).append("You must be a player to use this command");
                break;
            }
            case 9: {
                message.append(ChatColor.WHITE).append("Current timezone: ").append(args[0]);
                break;
            }
            case 10: {
                message.append(ChatColor.GREEN).append("New timezone: ").append(args[0]);
                break;
            }
            case 11: {
                message.append(ChatColor.RED).append("Timezone '").append(args[0]).append("' is not valid\n").append(ChatColor.GOLD).append("| ").append(ChatColor.WHITE).append("Please look at 'timezone.txt' in the\n").append(ChatColor.GOLD).append("| ").append(ChatColor.WHITE).append("plugin folder");
                break;
            }
            default: {
                message.append(ChatColor.RED).append("THIS MESSAGE SHOULD NOT EXIST");
                break;
            }
        }
        message.append("\n").append(ChatColor.GOLD).append("===========================================");
        return message.toString();
    }

}
