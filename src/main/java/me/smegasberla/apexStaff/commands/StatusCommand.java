package me.smegasberla.apexStaff.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import me.smegasberla.apexStaff.ApexStaff;
import me.smegasberla.apexStaff.managers.DatabaseManager;
import me.smegasberla.apexStaff.managers.StatusManager;
import me.smegasberla.apexStaff.utils.MessageUtils;

public class StatusCommand implements CommandExecutor {

    private final ApexStaff plugin;
    private final DatabaseManager databaseManager;
    private final StatusManager manager;

    public StatusCommand(ApexStaff plugin, DatabaseManager databaseManager, StatusManager manager) {

        this.databaseManager = databaseManager;
        this.plugin = plugin;
        this.manager = manager;

    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,@NotNull String[] args) {
        
        String wrongArgs = MessageUtils.getMessage(plugin, "wrong-arguments");
        String onlyPlayers = MessageUtils.getMessage(plugin, "only-players");

        if (!(sender instanceof Player)) {
            if (onlyPlayers != null) {

                sender.sendMessage(onlyPlayers);

            }
        }

        Player p = (Player) sender;
        UUID playerUUID = p.getUniqueId();

        if (args.length < 1) {
            if (wrongArgs != null) {
                p.sendMessage(wrongArgs);
                return true;
            }
        }

        String targetName = args[0];
        Player target = Bukkit.getPlayerExact(targetName);
        if(target == null) {

            String playerOffline = MessageUtils.getMessage(plugin, "player-offline", "{target}", targetName);
            p.sendMessage(playerOffline);

        }

        UUID targetUUID = Bukkit.getOfflinePlayer(targetName).getUniqueId();

        manager.statusMethod(p, target);
        

        return true;
    }

    
    

    
}
