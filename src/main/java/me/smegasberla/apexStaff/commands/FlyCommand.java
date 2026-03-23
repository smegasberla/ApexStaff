package me.smegasberla.apexStaff.commands;

import me.smegasberla.apexStaff.ApexStaff;
import me.smegasberla.apexStaff.managers.FlyManager;
import me.smegasberla.apexStaff.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class FlyCommand implements CommandExecutor {

    private final ApexStaff plugin;
    private final FlyManager manager;

    public FlyCommand(ApexStaff plugin, FlyManager manager) {
        this.plugin = plugin;

        this.manager = manager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        String wrongArgs = MessageUtils.getMessage(plugin, "wrong-arguments");
        String onlyPlayers = MessageUtils.getMessage(plugin, "only-players");


        if(!(sender instanceof Player)) {

            if(onlyPlayers != null) {

                sender.sendMessage(onlyPlayers);
                return true;

            }

        }

        Player p = (Player) sender;

        if(args.length > 1) {
            if(wrongArgs != null) {
                p.sendMessage(wrongArgs);
                return true;
            }

        }

        if(args.length == 0) {

            String enteredFly = MessageUtils.getMessage(plugin, "entered-fly");
            String exitedFly = MessageUtils.getMessage(plugin, "exited-fly");

            manager.toggleFly(p);

            if(manager.customFlight.contains(p.getUniqueId())) {

                if(enteredFly != null) {

                    p.sendMessage(enteredFly);
                    return true;

                }

            } else {

                if(enteredFly != null) {

                    p.sendMessage(exitedFly);
                    return true;

                }

            }

        }

        if(args.length == 1) {

            String targetMessageEnter = MessageUtils.getMessage(plugin, "target-message-entered", "{player}", p.getName());
            String targetMessageExit = MessageUtils.getMessage(plugin, "target-message-exited", "{player}", p.getName());

            String targetName = args[0];
            Player target = Bukkit.getPlayerExact(targetName);

            if (target == null) {
                String playerOffline = MessageUtils.getMessage(plugin, "player-offline", "{target}", targetName);
                p.sendMessage(playerOffline);
                return true;
            }

            String enteredFly = MessageUtils.getMessage(plugin, "target-entered-fly", "{target}", targetName);
            String exitedFly = MessageUtils.getMessage(plugin, "target-exited-fly", "{target}", targetName);

            manager.toggleFly(target);

            if(manager.customFlight.contains(target.getUniqueId())) {

                if(enteredFly != null) {

                    p.sendMessage(enteredFly);
                    target.sendMessage(targetMessageEnter);
                    return true;

                }

            } else {

                if(exitedFly != null) {

                    p.sendMessage(exitedFly);
                    target.sendMessage(targetMessageExit);
                    return true;

                }

            }

        }

        return true;
    }
}
