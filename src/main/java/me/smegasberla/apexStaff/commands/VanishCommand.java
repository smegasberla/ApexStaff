package me.smegasberla.apexStaff.commands;

import me.smegasberla.apexStaff.ApexStaff;
import me.smegasberla.apexStaff.managers.VanishManager;
import me.smegasberla.apexStaff.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class VanishCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        ApexStaff plugin = ApexStaff.getPlugin();

        if (!(sender instanceof Player)) {
            String onlyPlayers = MessageUtils.getMessage(plugin, "only-players");
            if (onlyPlayers != null) {
                sender.sendMessage(onlyPlayers);
            }
            return true;
        }

        Player p = (Player) sender;

        if (args.length > 1) {
            String wrongArgs = MessageUtils.getMessage(plugin, "wrong-arguments");
            p.sendMessage(wrongArgs);
            return true;
        }

        if (args.length == 1) {

            String targetName = args[0];
            Player target = Bukkit.getPlayerExact(targetName);

            if (target == null) {

                String playerOffline = MessageUtils.getMessage(plugin, "player-offline", "{target}", targetName);
                p.sendMessage(playerOffline);
                return true;
            }

            String targetVanished = MessageUtils.getMessage(plugin, "target-vanished", "{player}", p.getName());
            String targetUnvanished = MessageUtils.getMessage(plugin, "target-unvanished", "{player}", p.getName());
            String successfullyVanished = MessageUtils.getMessage(plugin, "succesfully-vanished", "{target}", target.getName());
            String successfullyUnvanished = MessageUtils.getMessage(plugin, "successfully-unvanished", "{target}", target.getName());

            if (VanishManager.isVanished(target)) {
                VanishManager.unvanishPlayer(target);
                target.sendMessage(targetUnvanished);
                p.sendMessage(successfullyUnvanished);
            } else {
                VanishManager.vanishPlayer(target);
                target.sendMessage(targetVanished);
                p.sendMessage(successfullyVanished);
            }

            return true;
        }

        if (args.length == 0) {

            String playerVanished = MessageUtils.getMessage(plugin, "vanish-message");
            String playerUnvanished = MessageUtils.getMessage(plugin, "exit-vanish");

            if (VanishManager.isVanished(p)) {
                VanishManager.unvanishPlayer(p);
                p.sendMessage(playerUnvanished);
            } else {
                VanishManager.vanishPlayer(p);
                p.sendMessage(playerVanished);
            }

            return true;
        }

        return true;
    }
}