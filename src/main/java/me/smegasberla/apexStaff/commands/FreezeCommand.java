package me.smegasberla.apexStaff.commands;

import me.smegasberla.apexStaff.ApexStaff;
import me.smegasberla.apexStaff.managers.FreezeManager;
import me.smegasberla.apexStaff.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class FreezeCommand implements CommandExecutor {

    public static String commandExecutor;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        commandExecutor = sender.getName();

        ApexStaff plugin = ApexStaff.getPlugin(ApexStaff.class);
        String wrongArgs = MessageUtils.getMessage(plugin, "wrong-arguments");
        String freezeYourself = MessageUtils.getMessage(plugin, "freeze-yourself");

        if(!(sender instanceof Player)) {

            String onlyPlayers = MessageUtils.getMessage(plugin, "only-players");
            if (onlyPlayers != null) {
                sender.sendMessage(onlyPlayers);
            }
            return true;

        }

        Player p = (Player) sender;

        if (args.length == 0) {
            if(freezeYourself != null) {

                p.sendMessage(freezeYourself);

            }
            return true;
        }

        if (args.length > 1) {
            if(wrongArgs != null) {

                p.sendMessage(wrongArgs);
                return true;

            }
        }

        String freezedMessage = MessageUtils.getMessage(plugin, "freezed-message", "{player}", p.getName());
        String unfreezedMessage = MessageUtils.getMessage(plugin, "unfreezed-message", "{player}", p.getName());

        String targetName = args[0];
        Player target = Bukkit.getPlayerExact(targetName);
        if(target == null) {

            String playerOffline = MessageUtils.getMessage(plugin, "player-offline", "{target}", targetName);
            p.sendMessage(playerOffline);
            return true;

        }
        UUID targetUUID = target.getUniqueId();

        if(targetName.equalsIgnoreCase(p.getName())) {

            if(freezeYourself != null) {

                p.sendMessage(freezeYourself);
                return true;
            }


        }

        String successfullyFreezed = MessageUtils.getMessage(plugin, "successfully-freezed", "{target}", targetName);
        String successfullyUnfreezed = MessageUtils.getMessage(plugin, "successfully-unfreezed", "{target}", targetName);

        if(FreezeManager.isFreezed(target)) {

            FreezeManager.unfreezePlayer(target);
            p.sendMessage(successfullyUnfreezed);
            target.sendMessage(unfreezedMessage);
            return true;

        } else {

            FreezeManager.freezePlayer(target);
            p.sendMessage(successfullyFreezed);
            target.sendMessage(freezedMessage);


        }

        return true;
    }
}
