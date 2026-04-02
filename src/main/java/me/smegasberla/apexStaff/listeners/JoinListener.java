package me.smegasberla.apexStaff.listeners;

import me.smegasberla.apexStaff.ApexStaff;
import me.smegasberla.apexStaff.managers.DatabaseManager;
import me.smegasberla.apexStaff.managers.DupeIPManager;
import me.smegasberla.apexStaff.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;
import java.util.UUID;




public class JoinListener implements Listener {

    private final ApexStaff plugin;
    private final DupeIPManager manager;
    private final DatabaseManager databaseManager;

    public JoinListener(ApexStaff pluign, ApexStaff plugin, DupeIPManager manager, DatabaseManager databaseManager) {
        this.plugin = plugin;
        this.manager = manager;
        this.databaseManager = databaseManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {

        Player target = e.getPlayer();
        String targetName = target.getName();
        UUID targetUUID = target.getUniqueId();
        String staffUUID = databaseManager.getStaffUUID(targetUUID);
        int numberOfNotes = databaseManager.getNoteCount(targetUUID);
        int minNotes = plugin.getConfig().getInt("notes.alert-for-noted-players.min-notes-threshold");
        String formattedNotesNumber = String.valueOf(numberOfNotes);

        boolean enabled = plugin.getConfig().getBoolean("notes.alert-for-noted-players.enabled");
        boolean alertOnJoin = plugin.getConfig().getBoolean("notes.alert-for-noted-players.alert-on-join");
        boolean alertOnAltJoin = plugin.getConfig().getBoolean("notes.alert-for-noted-players.alert-on-alt-join");

        if (enabled && staffUUID != null) {
            Player staff = Bukkit.getPlayer(UUID.fromString(staffUUID));

            if (staff != null) {

                if(numberOfNotes > minNotes) {

                    if (alertOnJoin) {
                        String alertMessage = MessageUtils.getMessage(plugin, "join-notes-alert",
                                "{target}", targetName,
                                "{number_of_notes}", formattedNotesNumber
                        );

                        if (alertMessage != null) {
                            staff.sendMessage(alertMessage);
                        }
                    }

                    if (alertOnAltJoin) {
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

                            String alertMessage = MessageUtils.getMessage(plugin, "alt-join-notes-alert",
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

        UUID uuid = e.getPlayer().getUniqueId();
        String currentIP = e.getPlayer().getAddress().getHostString();
        manager.playerIP.put(uuid, currentIP);
        databaseManager.upsertPlayer(uuid, currentIP);


    }

}
