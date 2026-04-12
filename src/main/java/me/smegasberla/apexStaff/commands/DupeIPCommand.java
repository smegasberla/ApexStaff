package me.smegasberla.apexStaff.commands;

import me.smegasberla.apexStaff.ApexStaff;
import me.smegasberla.apexStaff.managers.DatabaseManager;
import me.smegasberla.apexStaff.utils.MessageUtils;
import me.smegasberla.apexStaff.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DupeIPCommand implements CommandExecutor {

    private final ApexStaff plugin;
    private final DatabaseManager databaseManager;

    public DupeIPCommand(ApexStaff plugin, DatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
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

        if (args.length == 0) {
            UUID uuid = p.getUniqueId();
            List<String> altList = databaseManager.getAlts(uuid);
            List<String> coloredAlts = new ArrayList<>();

            for (String altName : altList) {
                long lastSeen = databaseManager.getLastSeenByName(altName);
                String formattedLastSeen = TimeUtils.formatTimestamp(lastSeen);

                String onlineStatus = MessageUtils.getMessage(plugin, "alt-online", "{alt}", altName);
                String offlineStatus = MessageUtils.getMessage(plugin, "alt-offline", "{alt}", altName, "{last_seen}", formattedLastSeen);

                String noColorAlt = ChatColor.stripColor(altName);
                boolean isOnline = Bukkit.getPlayerExact(noColorAlt) != null;

                if (onlineStatus == null || offlineStatus == null) continue;

                if (isOnline) {
                    coloredAlts.add(onlineStatus);
                } else {
                    coloredAlts.add(offlineStatus);
                }
            }

            String alts = String.join("\n", coloredAlts);
            String IP = p.getAddress().getHostString();
            int count = databaseManager.getAltCount(uuid);

            String dupeIPMessage = MessageUtils.getMessage(plugin, "dupeip-message",
                    "{player}", p.getName(),
                    "{ip}", IP,
                    "{count}", String.valueOf(count),
                    "{alt_list}", alts
            );

            if (dupeIPMessage != null) p.sendMessage(dupeIPMessage);
            return true;
        }

        if (args.length == 1) {
            String targetName = args[0];
            Player target = Bukkit.getPlayerExact(targetName);

            if (target == null) {
                String playerOffline = MessageUtils.getMessage(plugin, "player-offline", "{target}", targetName);
                if (playerOffline != null) p.sendMessage(playerOffline);
                return true;
            }

            UUID uuid = target.getUniqueId();
            List<String> altList = databaseManager.getAlts(uuid);
            List<String> coloredAlts = new ArrayList<>();

            for (String altName : altList) {
                long lastSeen = databaseManager.getLastSeenByName(altName);
                String formattedLastSeen = TimeUtils.formatTimestamp(lastSeen);

                String noColorAlt = ChatColor.stripColor(altName);
                boolean isOnline = Bukkit.getPlayerExact(noColorAlt) != null;

                String onlineStatus = MessageUtils.getMessage(plugin, "alt-online", "{alt}", altName);
                String offlineStatus = MessageUtils.getMessage(plugin, "alt-offline", "{alt}", altName, "{last_seen}", formattedLastSeen);

                if (onlineStatus == null || offlineStatus == null) continue;

                if (isOnline) {
                    coloredAlts.add(onlineStatus);
                } else {
                    coloredAlts.add(offlineStatus);
                }
            }

            String alts = String.join("\n", coloredAlts);
            String IP = target.getAddress().getHostString();
            int count = databaseManager.getAltCount(uuid);

            String dupeIPMessage = MessageUtils.getMessage(plugin, "target-dupeip-message",
                    "{target}", targetName,
                    "{ip}", IP,
                    "{count}", String.valueOf(count),
                    "{alt_list}", alts
            );

            if (dupeIPMessage != null) p.sendMessage(dupeIPMessage);
        }

        return true;
    }
}