package it.blank517.realtimeworld;

import org.bukkit.ChatColor;

import java.util.List;

class Messages {

    private final RealTimeWorld plugin;

    Messages(RealTimeWorld plugin) {
        this.plugin = plugin;
    }

    String get(int type) {
        return get(type, "");
    }

    String get(int type, String text1) {
        String message = "\n" + ChatColor.GOLD + "============" + ChatColor.YELLOW + " RealTimeWorld " + ChatColor.GOLD + "============\n";
        switch (type) {
            case -1:
                message += ChatColor.RED + "You can't use this command!";
                break;
            case 0:
                message += "| " + ChatColor.YELLOW + "/" + text1 + ChatColor.AQUA + " enable" + ChatColor.WHITE + " - Enable the daylight synchronization\n" +
                        ChatColor.GOLD + "| " + ChatColor.YELLOW + "/" + text1 + ChatColor.AQUA + " disable" + ChatColor.WHITE + " - Disable the daylight synchronization\n" +
                        ChatColor.GOLD + "| " + ChatColor.YELLOW + "/" + text1 + ChatColor.AQUA + " whitelist list\n" +
                        ChatColor.GOLD + "| " + ChatColor.YELLOW + "/" + text1 + ChatColor.AQUA + " whitelist add " + ChatColor.GREEN + "<world>\n" +
                        ChatColor.GOLD + "| " + ChatColor.YELLOW + "/" + text1 + ChatColor.AQUA + " whitelist remove " + ChatColor.GREEN + "<world>\n";
                break;
            case 1:
                message += ChatColor.GOLD + "| " + ChatColor.GREEN + "Time synchronization enabled\n";
                break;
            case 2:
                message += ChatColor.GOLD + "| " + ChatColor.GREEN + "Time synchronization is already enabled\n";
                break;
            case 3:
                message += ChatColor.GOLD + "| " + ChatColor.RED + "Time synchronization disabled\n";
                break;
            case 4:
                message += ChatColor.GOLD + "| " + ChatColor.RED + "Time synchronization is already disabled\n";
                break;
            case 5:
                List<String> whitelist = plugin.getCustomConfig().getWhitelist();
                StringBuilder sbMessage = new StringBuilder(message)
                        .append("| Worlds Whitelist:\n");
                for (String world : whitelist) {
                    sbMessage.append(ChatColor.GOLD).append("|").append(ChatColor.WHITE).append(" - ").append(world).append("\n");
                }
                message = sbMessage.toString();
                break;
            case 6:
                message += ChatColor.GOLD + "| " + ChatColor.WHITE + "Added to whitelist: '" + text1 + "'\n";
                break;
            case 7:
                message += ChatColor.GOLD + "| " + ChatColor.WHITE + "Removed from whitelist: '" + text1 + "'\n";
                break;
            default:
                message += ChatColor.GOLD + "| " + ChatColor.RED + "THIS MESSAGE SHOULD NOT EXIST\n";
        }
        message += ChatColor.GOLD + "=======================================";
        return message;
    }

}
