package me.smegasberla.apexStaff.commands;

import me.smegasberla.apexStaff.ApexStaff;
import me.smegasberla.apexStaff.managers.*;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ApexCommand implements CommandExecutor, TabCompleter {

    private final XRayCheckManager manager;
    private final FlyManager flyManager;
    private final DatabaseManager databaseManager;
    private final ShadowCamManager shadowCamManager;
    private final StaffChatManager staffChatManager;

    public ApexCommand(XRayCheckManager manager, FlyManager flyManager, DatabaseManager databaseManager, ShadowCamManager shadowCamManager, StaffChatManager staffChatManager) {
        this.manager = manager;
        this.flyManager = flyManager;
        this.databaseManager = databaseManager;
        this.shadowCamManager = shadowCamManager;
        this.staffChatManager = staffChatManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        ApexStaff plugin = ApexStaff.getPlugin(ApexStaff.class);
        String noPermission = ChatColor.translateAlternateColorCodes('&',
                plugin.getConfig().getString("no-permission", "&cNo permission."));

        if (args.length == 0) {
            plugin.sendHelpMessage(sender);
            return true;
        }

        String subCommand = args[0].toLowerCase();
        String[] subArgs = Arrays.copyOfRange(args, 1, args.length);

        switch (subCommand) {
            case "reload":
                if (sender.hasPermission("apexstaff.reload")) {
                    new ReloadCommand().onCommand(sender, command, label, subArgs);
                } else {
                    sender.sendMessage(noPermission);
                }
                break;

            case "vanish":
                if (sender.hasPermission("apexstaff.vanish")) {
                    new VanishCommand().onCommand(sender, command, label, subArgs);
                } else {
                    sender.sendMessage(noPermission);
                }
                break;

            case "freeze":
                if (sender.hasPermission("apexstaff.freeze")) {
                    new FreezeCommand().onCommand(sender, command, label, subArgs);
                } else {
                    sender.sendMessage(noPermission);
                }
                break;

            case "xray":
                if (sender.hasPermission("apexstaff.xray")) {
                    new XrayCommand(plugin, manager).onCommand(sender, command, label, subArgs);
                } else {
                    sender.sendMessage(noPermission);
                }
                break;

            case "fly":
                if (sender.hasPermission("apexstaff.fly")) {
                    new FlyCommand(plugin, flyManager).onCommand(sender, command, label, subArgs);
                } else {
                    sender.sendMessage(noPermission);
                }
                break;

            case "clearchat":
                if (sender.hasPermission("apexstaff.clearchat")) {
                    new ClearChatCommand(plugin).onCommand(sender, command, label, subArgs);
                } else {
                    sender.sendMessage(noPermission);
                }
                break;

            case "dupeip":
                if (sender.hasPermission("apexstaff.dupeip")) {
                    new DupeIPCommand(plugin, databaseManager).onCommand(sender, command, label, subArgs);
                } else {
                    sender.sendMessage(noPermission);
                }
                break;

            case "ping":
                if (sender.hasPermission("apexstaff.ping")) {
                    new PingCommand(plugin).onCommand(sender, command, label, subArgs);
                } else {
                    sender.sendMessage(noPermission);
                }
                break;

            case "shadowcam":
                if (sender.hasPermission("apexstaff.shadowcam")) {
                    new ShadowCamCommand(plugin, shadowCamManager).onCommand(sender, command, label, subArgs);
                } else {
                    sender.sendMessage(noPermission);
                }
                break;

            case "notes":
                if (sender.hasPermission("apexstaff.notes")) {
                    new NotesCommand(plugin, databaseManager).onCommand(sender, command, label, subArgs);
                } else {
                    sender.sendMessage(noPermission);
                }
                break;

            case "staffchat":
                if (sender.hasPermission("apexstaff.staffchat")) { // Note: Check if permission should be .staffchat
                    new StaffChatCommand(plugin, staffChatManager).onCommand(sender, command, label, subArgs);
                } else {
                    sender.sendMessage(noPermission);
                }
                break;

            case "exstension":
                if (sender.hasPermission("apexstaff.exstension")) {
                    new ExstensionCommand(plugin).onCommand(sender, command, label, subArgs);
                } else {
                    sender.sendMessage(noPermission);
                }
                break;

            default:
                plugin.sendHelpMessage(sender);
                break;
        }

        return true;

        
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            List<String> subCommands = Arrays.asList(
                    "reload", "vanish", "freeze", "xray", "fly", "clearchat",
                    "dupeip", "ping", "shadowcam", "notes", "staffchat", "exstension"
            );

            String partial = args[0].toLowerCase();
            for (String subCmd : subCommands) {
                if (subCmd.startsWith(partial)) {
                    completions.add(subCmd);
                }
            }
        } else if (args.length == 2) {
            String subCommand = args[0].toLowerCase();
            String partial = args[1].toLowerCase();

            List<String> playerCommands = Arrays.asList("vanish", "freeze", "xray", "fly", "clearchat", "dupeip", "ping", "shadowcam", "notes", "exstension");

            if (playerCommands.contains(subCommand)) {
                for (Player onlinePlayer : org.bukkit.Bukkit.getOnlinePlayers()) {
                    String name = onlinePlayer.getName();
                    if (name.toLowerCase().startsWith(partial)) {
                        completions.add(name);
                    }
                }
            }
        } else if (args.length == 3) {
            String subCommand = args[0].toLowerCase();
            String partial = args[2].toLowerCase();

            if (subCommand.equals("notes")) {
                List<String> noteSubCommands = Arrays.asList("add", "remove", "clear", "list");
                for (String noteCmd : noteSubCommands) {
                    if (noteCmd.startsWith(partial)) {
                        completions.add(noteCmd);
                    }
                }
            } else if (subCommand.equals("xray")) {
                List<String> xraySubCommands = Arrays.asList("info", "clear");
                for (String xrayCmd : xraySubCommands) {
                    if (xrayCmd.startsWith(partial)) {
                        completions.add(xrayCmd);
                    }
                }
            } else if (subCommand.equals("exstension")) {
                List<String> exstensionSubCommands = Arrays.asList("list", "reload", "info");
                for (String exstensionCmd : exstensionSubCommands) {
                    if (exstensionCmd.startsWith(partial)) {
                        completions.add(exstensionCmd);
                    }
                }
            }
        }

        return completions;
    }
}