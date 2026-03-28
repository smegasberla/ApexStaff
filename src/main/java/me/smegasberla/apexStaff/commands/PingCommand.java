package me.smegasberla.apexStaff.commands;

import me.smegasberla.apexStaff.ApexStaff;
import me.smegasberla.apexStaff.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PingCommand implements CommandExecutor {

    private final ApexStaff plugin;

    public PingCommand(ApexStaff plugin) {
        this.plugin = plugin;
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

        if (args.length > 1) {
            if (wrongArgs != null) {
                p.sendMessage(wrongArgs);
                return true;
            }
        }

        if(args.length == 0) {

            String ping = String.valueOf(p.getPing());

            String pingMessage = MessageUtils.getMessage(plugin, "ping-message",
                    "{player}", p.getName(),
                    "{ping}", ping
            );

            if(pingMessage != null) {

                p.sendMessage(pingMessage);

            }

            return true;

        }

        if(args.length == 1) {

            String targetName = args[0];
            Player target = Bukkit.getPlayerExact(targetName);
            if (target == null) {
                String playerOffline = MessageUtils.getMessage(plugin, "player-offline", "{target}", targetName);
                if (playerOffline != null) p.sendMessage(playerOffline);
                return true;
            }

            String ping = String.valueOf(target.getPing());

            String pingMessage = MessageUtils.getMessage(plugin, "target-ping-message",
                    "{target}", targetName,
                    "{ping}", ping
            );

            if(pingMessage != null) {

                p.sendMessage(pingMessage);

            }

            return true;

        }

        return true;
    }
}
