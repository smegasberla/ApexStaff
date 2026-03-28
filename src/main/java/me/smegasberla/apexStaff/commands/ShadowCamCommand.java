package me.smegasberla.apexStaff.commands;

import me.smegasberla.apexStaff.ApexStaff;
import me.smegasberla.apexStaff.managers.ShadowCamManager;
import me.smegasberla.apexStaff.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ShadowCamCommand implements CommandExecutor {

    private final ApexStaff plugin;
    private final ShadowCamManager manager;

    public ShadowCamCommand(ApexStaff plugin, ShadowCamManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        String wrongArgs = MessageUtils.getMessage(plugin, "wrong-arguments");
        String onlyPlayers = MessageUtils.getMessage(plugin, "only-players");

        if (!(sender instanceof Player)) {
            if(onlyPlayers != null) {

                sender.sendMessage(onlyPlayers);

            }
        }

        Player p = (Player) sender;
        UUID playerUUID = p.getUniqueId();

        if (args.length > 1) {
            if (wrongArgs != null) {
                p.sendMessage(wrongArgs);
                return true;
            }
        }

        if(args.length == 0) {

            String message = MessageUtils.getMessage(plugin, "shadow-cam-yourself");
            if(message != null) {

                p.sendMessage(message);
                return true;
            }


        }

        if(args.length == 1) {

            String targetName = args[0];
            Player target = Bukkit.getPlayerExact(targetName);

            if (target == null) {
                String playerOffline = MessageUtils.getMessage(plugin, "player-offline", "{target}", targetName);
                if (playerOffline != null) p.sendMessage(playerOffline);
                return true;
            }

            UUID targetUUID = target.getUniqueId();

            if(targetName.equals(p.getName())) {

                String message = MessageUtils.getMessage(plugin, "shadow-cam-yourself");
                if(message != null) {

                    p.sendMessage(message);
                    return true;

                }

            }

            String startShadowCam =  MessageUtils.getMessage(plugin, "started-shadow-cam", "{target}", targetName);
            String exitShadowCam =  MessageUtils.getMessage(plugin, "stopped-shadow-cam", "{target}", targetName);



            if (manager.isASpectator.contains(playerUUID)) {
                manager.stopShadowCam(playerUUID);

                if (exitShadowCam != null) {
                    p.sendMessage(exitShadowCam);
                }
                return true;
            }


            manager.startShadowCam(playerUUID, targetUUID);

            if (startShadowCam != null) {
                p.sendMessage(startShadowCam);
            }




        }

        return true;
    }
}
