package me.smegasberla.apexStaff.commands;

import me.smegasberla.apexStaff.ApexStaff;
import me.smegasberla.apexStaff.managers.DatabaseManager;
import me.smegasberla.apexStaff.managers.XRayCheckManager;
import me.smegasberla.apexStaff.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class XrayCommand implements CommandExecutor, TabCompleter {

    private final ApexStaff plugin;
    private final XRayCheckManager manager;

    public XrayCommand(ApexStaff plugin, XRayCheckManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        String wrongArgs = MessageUtils.getMessage(plugin, "wrong-arguments");
        String onlyPlayers = MessageUtils.getMessage(plugin, "only-players");


        if (!(sender instanceof Player)) return true;

        Player p = (Player) sender;

        if (args.length != 2) {

            p.sendMessage(wrongArgs);
            return true;
        }

        String targetName = args[0];
        Player target = Bukkit.getPlayerExact(targetName);
        if (target == null) {
            String playerOffline = MessageUtils.getMessage(plugin, "player-offline", "{target}", targetName);
            p.sendMessage(playerOffline);
            return true;

        }

        UUID targetUUID = target.getUniqueId();

        String subArg = args[1];

        switch (subArg) {
            case "info":

                if (target.hasPermission("apexstaff.exempt")) {
                    String exemptMessage = MessageUtils.getMessage(plugin, "exempt-message");

                    if (exemptMessage != null) {
                        p.sendMessage(exemptMessage);
                        return true;
                    }

                }

                int blocks = manager.totalBlocks.getOrDefault(targetUUID, 0);
                int ores = manager.totalOres.getOrDefault(targetUUID, 0);
                double percentage = manager.calcutaleXRayPercentage(target);


                double alertThreshold = plugin.getConfig().getDouble("xray.alert-threshold");
                double highRiskDouble = plugin.getConfig().getDouble("xray.high-risk");


                boolean isSuspect = manager.isSuspect(target);
                String suspectStatus = isSuspect
                        ? MessageUtils.getMessage(plugin, "xray-suspect.suspect")
                        : MessageUtils.getMessage(plugin, "xray-suspect.clear");


                String risk;
                if (!isSuspect) {

                    risk = MessageUtils.getMessage(plugin, "xray-risk-level.low");
                } else {

                    if (percentage < alertThreshold) {
                        risk = MessageUtils.getMessage(plugin, "xray-risk-level.low");
                    } else if (percentage < highRiskDouble) {
                        risk = MessageUtils.getMessage(plugin, "xray-risk-level.moderate");
                    } else {
                        risk = MessageUtils.getMessage(plugin, "xray-risk-level.high");
                    }
                }


                String infoMessage = MessageUtils.getMessage(plugin, "xray-message",
                        "{target}", targetName,
                        "{total_blocks}", String.valueOf(blocks),
                        "{total_ores}", String.valueOf(ores),
                        "{percentage_of_xray}", String.format("%.2f", percentage),
                        "{risk_of_xray}", risk,
                        "{suspect_status}", suspectStatus
                );

                if (infoMessage != null) p.sendMessage(infoMessage);
                return true;

            case "clear":

                if (target.hasPermission("apexstaff.exempt")) {
                    String exemptMessage = MessageUtils.getMessage(plugin, "exempt-message");

                    if (exemptMessage != null) {
                        p.sendMessage(exemptMessage);
                        return true;
                    }

                }

                DatabaseManager.removeXrayData(target);
                manager.totalBlocks.remove(targetUUID);
                manager.totalOres.remove(targetUUID);

                String clearedData = MessageUtils.getMessage(plugin, "cleared-xray-data", "{target}", targetName);
                if (clearedData != null) p.sendMessage(clearedData);
                return true;

            default:
                p.sendMessage(wrongArgs);
                return true;
        }
    }
    
        @Override
        public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
            List<String> completions = new ArrayList<>();
    
            if (args.length == 1) {
                String partial = args[0].toLowerCase();
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    String name = onlinePlayer.getName();
                    if (name.toLowerCase().startsWith(partial)) {
                        completions.add(name);
                    }
                }
            } else if (args.length == 2) {
                String partial = args[1].toLowerCase();
                List<String> xraySubCommands = Arrays.asList("info", "clear");
                for (String xrayCmd : xraySubCommands) {
                    if (xrayCmd.startsWith(partial)) {
                        completions.add(xrayCmd);
                    }
                }
            }
    
            return completions;
        }
    
    }


