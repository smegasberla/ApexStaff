package me.smegasberla.apexStaff.commands;

import me.smegasberla.apexStaff.ApexStaff;
import me.smegasberla.exstension.ExstensionWrapper;
import me.smegasberla.exstension.loader.ExstensionLoader;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExstensionCommand implements CommandExecutor, TabCompleter {

    private final ApexStaff plugin;

    public ExstensionCommand(ApexStaff plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        String noPermission = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("no-permission", "&cNo permission."));

        if (!sender.hasPermission("apexstaff.exstension")) {
            sender.sendMessage(noPermission);
            return true;
        }

        if (args.length == 0) {
            listExstensions(sender);
            return true;
        }

        String subCommand = args[0].toLowerCase();
        String[] subArgs = Arrays.copyOfRange(args, 1, args.length);

        switch (subCommand) {
            case "list":
                listExstensions(sender);
                break;
            case "reload":
                reloadExstensions(sender);
                break;
            case "info":
                showInfo(sender, subArgs);
                break;
            default:
                sendHelp(sender);
                break;
        }

        return true;
    }

private void listExstensions(CommandSender sender) {
    var manager = ExstensionLoader.getInstance();
    if (manager == null) {
        sendMessage(sender, "exstensions.none-loaded", "&cNo exstensions loaded.");
        return;
    }

    var exstensions = manager.getLoadedExstensions();
    if (exstensions.isEmpty()) {
        sendMessage(sender, "exstensions.none-loaded", "&cNo exstensions loaded.");
        return;
    }

    String header = getPlainMessage("exstensions.list-header", "&8--- &6Exstensions &8(&e{count}&8) &6---");
    header = header.replace("{count}", String.valueOf(exstensions.size()));
    sender.sendMessage(header);
    for (ExstensionWrapper wrapper : exstensions) {
        String message = ChatColor.translateAlternateColorCodes('&', "&e" + wrapper.getName() + " &7v" + wrapper.getVersion());
        sender.sendMessage(message);
    }
}

private void reloadExstensions(CommandSender sender) {
    ExstensionLoader.disableAll();
    ExstensionLoader.load(plugin);
    ExstensionLoader.enableAll(plugin);
    sendMessage(sender, "exstensions.reloaded", "&aExstensions reloaded!");
}

private void showInfo(CommandSender sender, String[] args) {
    if (args.length == 0) {
        sendHelp(sender);
        return;
    }

    String name = args[0];
    var manager = ExstensionLoader.getInstance();
    if (manager == null) {
        sendMessage(sender, "exstensions.not-found", "&cExstension not found.");
        return;
    }

    var wrapper = manager.getExstensionByName(name);
    if (wrapper == null) {
        sendMessage(sender, "exstensions.not-found", "&cExstension '" + name + "' not found.");
        return;
    }

    String header = getPlainMessage("exstensions.info-header", "&8--- &6{name} Info &8---");
    header = header.replace("{name}", name);
    sender.sendMessage(header);
    sender.sendMessage(ChatColor.GOLD + "Name: " + ChatColor.YELLOW + wrapper.getName());
    sender.sendMessage(ChatColor.GOLD + "Version: " + ChatColor.YELLOW + wrapper.getVersion());
    sender.sendMessage(ChatColor.GOLD + "Main Class: " + ChatColor.YELLOW + wrapper.getMainClass());
    sender.sendMessage(ChatColor.GOLD + "File: " + ChatColor.YELLOW + wrapper.getFile().getName());
}

private void sendHelp(CommandSender sender) {
    String header = getPlainMessage("exstensions.help-header", "&8--- &6Exstension Help &8---");
    sender.sendMessage(header);
    sender.sendMessage(ChatColor.YELLOW + "/apexstaff exstension list &7- List all exstensions");
    sender.sendMessage(ChatColor.YELLOW + "/apexstaff exstension reload &7- Reload all exstensions");
    sender.sendMessage(ChatColor.YELLOW + "/apexstaff exstension info <name> &7- Show info about an exstension");
}

private void sendMessage(CommandSender sender, String path, String defaultMessage) {
    String message = plugin.getConfig().getString("messages." + path, defaultMessage);
    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
}

private String getPlainMessage(String path, String defaultMessage) {
    String message = plugin.getConfig().getString("messages." + path, defaultMessage);
    return ChatColor.translateAlternateColorCodes('&', message);
}

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            List<String> subCommands = Arrays.asList("list", "reload", "info");
            String partial = args[0].toLowerCase();

            for (String subCmd : subCommands) {
                if (subCmd.startsWith(partial)) {
                    completions.add(subCmd);
                }
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("info")) {
            String partial = args[1].toLowerCase();
            var manager = ExstensionLoader.getInstance();
            if (manager != null) {
                for (ExstensionWrapper wrapper : manager.getLoadedExstensions()) {
                    String name = wrapper.getName();
                    if (name.toLowerCase().startsWith(partial)) {
                        completions.add(name);
                    }
                }
            }
        }

        return completions;
    }
}
