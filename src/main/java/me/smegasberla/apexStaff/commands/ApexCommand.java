package me.smegasberla.apexStaff.commands;

import me.smegasberla.apexStaff.ApexStaff;
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

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

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
            List<String> subCommands = Arrays.asList("reload", "vanish");
            String partial = args[0].toLowerCase();

            for (String subCmd : subCommands) {
                if (subCmd.startsWith(partial)) {
                    completions.add(subCmd);
                }
            }
        }

        return completions;
    }
}
