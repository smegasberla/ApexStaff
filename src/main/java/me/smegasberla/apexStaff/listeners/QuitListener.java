package me.smegasberla.apexStaff.listeners;

import me.smegasberla.apexStaff.ApexStaff;
import me.smegasberla.apexStaff.commands.FreezeCommand;
import me.smegasberla.apexStaff.managers.*;
import me.smegasberla.apexStaff.models.FreezeModel;
import me.smegasberla.apexStaff.utils.MessageUtils;
import me.smegasberla.apexStaff.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;
import java.util.UUID;

public class QuitListener implements Listener {

    private final ApexStaff plugin;
    private final DatabaseManager databaseManager;
    private final XRayCheckManager xRayCheckManager;
    private final DupeIPManager dupeIPManager;
    private final StaffChatManager staffChatManager;

    public QuitListener(ApexStaff plugin, DatabaseManager databaseManager, XRayCheckManager xRayCheckManager, DupeIPManager dupeIPManager, StaffChatManager staffChatManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        this.xRayCheckManager = xRayCheckManager;
        this.dupeIPManager = dupeIPManager;
        this.staffChatManager = staffChatManager;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {

        Player target = e.getPlayer();
        String targetName = target.getName();
        UUID targetUUID = target.getUniqueId();
        String staffUUID = databaseManager.getStaffUUID(targetUUID);
        int numberOfNotes = databaseManager.getNoteCount(targetUUID);
        int minNotes = plugin.getConfig().getInt("notes.alert-for-noted-players.min-notes-threshold");
        String formattedNotesNumber = String.valueOf(numberOfNotes);

        boolean enabled = plugin.getConfig().getBoolean("notes.alert-for-noted-players.enabled");
        boolean alertOnQuit = plugin.getConfig().getBoolean("notes.alert-for-noted-players.alert-on-quit");
        boolean alertOnAltQuit = plugin.getConfig().getBoolean("notes.alert-for-noted-players.alert-on-alt-quit");

        if(staffChatManager.isInStaffChat.contains(targetUUID)) {

            staffChatManager.isInStaffChat.remove(targetUUID);

        }

        if (enabled && staffUUID != null) {
            Player staff = Bukkit.getPlayer(UUID.fromString(staffUUID));

            if (staff != null) {

                if (numberOfNotes > minNotes) {

                    if (alertOnQuit) {
                        String alertMessage = MessageUtils.getMessage(plugin, "quit-notes-alert",
                                "{target}", targetName,
                                "{number_of_notes}", formattedNotesNumber
                        );

                        if (alertMessage != null) {
                            staff.sendMessage(alertMessage);
                        }
                    }

                    if (alertOnAltQuit) {
                        List<String> targetAlts = databaseManager.getAlts(targetUUID);

                        for (String alt : targetAlts) {
                            String altName = alt;
                            Player altPlayer = Bukkit.getPlayerExact(alt);

                            if (altPlayer != null) {
                                altName = altPlayer.getName();
                            }

                            UUID altUUID = altPlayer.getUniqueId();

                            int altNotes = databaseManager.getNoteCount(altUUID);
                            String formattedAltNotes =  String.valueOf(altNotes);

                            String alertMessage = MessageUtils.getMessage(plugin, "alt-quit-notes-alert",
                                    "{target}", alt,
                                    "{alt}", targetName,
                                    "{number_of_notes}", formattedAltNotes
                            );

                            if (alertMessage != null) {
                                staff.sendMessage(alertMessage);
                            }
                        }
                    }

                }

            }
        }

        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();

        long now = System.currentTimeMillis();

        dupeIPManager.lastSeen.put(uuid, now);
        dupeIPManager.playerIP.remove(uuid);

        databaseManager.updateLastSeen(uuid, now);

        int totalBlocks = xRayCheckManager.totalBlocks.getOrDefault(uuid, 0);
        int totalOres = xRayCheckManager.totalOres.getOrDefault(uuid, 0);

        DatabaseManager.addXrayData(p,totalBlocks, totalOres);

        if (FreezeManager.isFreezed(p)) {
            if (plugin.getConfig().getBoolean("freeze.ban-on-quit.enabled")) {

                String reason = MessageUtils.getMessage(plugin, "freeze.ban-on-quit.reason");
                String bannedBy = (FreezeCommand.commandExecutor != null) ? FreezeCommand.commandExecutor : "Console";

                long expiry = 0;
                if (plugin.getConfig().getBoolean("freeze.ban-on-quit.temp-ban.enabled")) {
                    String configDuration = plugin.getConfig().getString("freeze.ban-on-quit.temp-ban.time");
                    long rawDuration = TimeUtils.parseTimeToMillis(configDuration);
                    expiry = rawDuration + now;

                    DatabaseManager.addFreezeBan(p, reason, now, expiry, bannedBy);

                    FreezeModel.addBan(uuid, new FreezeModel(0, uuid.toString(), bannedBy, reason, now, expiry));

                } else {
                    DatabaseManager.addFreezeBan(p, reason, now, -1, bannedBy);
                    // ADD THIS: Cache the ban in memory
                    FreezeModel banEntry = new FreezeModel(0, uuid.toString(), bannedBy, reason, now, -1);
                    FreezeModel.addBan(uuid, banEntry);
                }
            }
        }
    }

}
