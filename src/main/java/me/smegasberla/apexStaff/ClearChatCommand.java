package me.smegasberla.apexStaff;

import me.smegasberla.apexStaff.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ClearChatCommand implements CommandExecutor {

    private final ApexStaff plugin;

    public ClearChatCommand(ApexStaff plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        String wrongArgs = MessageUtils.getMessage(plugin, "wrong-arguments");
        String onlyPlayers = MessageUtils.getMessage(plugin, "only-players");

        if(!(sender instanceof Player)) {

            if(onlyPlayers !=  null) {

                sender.sendMessage(onlyPlayers);
                return true;

            }

        }

        Player p = (Player) sender;
        String clearedChat = MessageUtils.getMessage(plugin, "cleared-chat");

        if(args.length  > 1) {

            if(wrongArgs != null) {

                p.sendMessage(wrongArgs);

            }

        }

        if(args.length == 0) {

            for(int i = 0; i < 100; i++) {

                p.sendMessage(" ");



            }
            //Here send eventual messages from config.yml
            if(clearedChat != null) {

                p.sendMessage(clearedChat);

            }

            return true;

        }

        String targetName = args[0];
        Player target = Bukkit.getPlayerExact(targetName);
        if(target == null) {

            String playerOffline = MessageUtils.getMessage(plugin, "player-offline", "{target}", targetName);
            p.sendMessage(playerOffline);

        }

        if(args.length == 1) {

            String targetClearedChat = MessageUtils.getMessage(plugin, "cleared-chat", "{player}", p.getName());
            String successfullyCleared = MessageUtils.getMessage(plugin, "successfully-cleared-chat", "{target}", targetName);

            for(int i = 0; i < 100; i++) {

                target.sendMessage(" ");

            }
            //Here send eventual messages from config.yml
            if(targetClearedChat != null) {

                target.sendMessage(targetClearedChat);

            }

            if(successfullyCleared != null) {

                p.sendMessage(successfullyCleared);

            }

        }

        return true;
    }
}
