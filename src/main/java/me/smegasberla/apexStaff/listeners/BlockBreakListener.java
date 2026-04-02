package me.smegasberla.apexStaff.listeners;

import me.smegasberla.apexStaff.ApexStaff;
import me.smegasberla.apexStaff.enums.MiningBlock;
import me.smegasberla.apexStaff.managers.DatabaseManager;
import me.smegasberla.apexStaff.managers.XRayCheckManager;
import me.smegasberla.apexStaff.utils.MessageUtils;
import me.smegasberla.apexStaff.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.bukkit.Bukkit.getLogger;

public class BlockBreakListener implements Listener {

  private final ApexStaff plugin;
  private final DatabaseManager databaseManager;
  private final XRayCheckManager manager;
  private final Set<UUID> activeXrayTimers = new HashSet<>();

  public BlockBreakListener(ApexStaff plugin, DatabaseManager databaseManager, XRayCheckManager manager) {
    this.plugin = plugin;
    this.databaseManager = databaseManager;
    this.manager = manager;
  }

  @EventHandler
  public void onPlayerBreak(BlockBreakEvent e) {
    Player target = e.getPlayer();

    if (target.hasPermission("apexstaff.exempt")) return;
    if (!plugin.getConfig().getBoolean("xray.enabled")) return;

    UUID targetUUID = target.getUniqueId();

    List<String> materialList = manager.getMaterialList();
    Material blockMaterial = e.getBlock().getType();

    if (MiningBlock.isMiningBlock(blockMaterial)) {
      manager.totalBlocks.put(targetUUID, manager.totalBlocks.getOrDefault(targetUUID, 0) + 1);
    }

    if (materialList.contains(blockMaterial.toString())) {
      manager.totalOres.put(targetUUID, manager.totalOres.getOrDefault(targetUUID, 0) + 1);
    }

    double alertThreshold = plugin.getConfig().getDouble("xray.alert-threshold");
    double percentage = manager.calcutaleXRayPercentage(target);

    if (percentage > alertThreshold && manager.isSuspect(target)) {
      if (plugin.getConfig().getBoolean("xray.automatic-alert")) {

        if (activeXrayTimers.contains(targetUUID)) return;
        activeXrayTimers.add(targetUUID);

        String timeString = plugin.getConfig().getString("xray.alert-interval", "2m");
        long seconds = TimeUtils.parseTimeString(timeString);

        if (seconds <= 0) {
          getLogger().warning("Invalid time format in config for 'xray.alert-interval'. Defaulting to 2m.");
          seconds = 120L;
        }

        long ticks = seconds * 20L;

        new BukkitRunnable() {
          @Override
          public void run() {
            if (target == null || !target.isOnline()) {
              activeXrayTimers.remove(targetUUID);
              this.cancel();
              return;
            }

            if (!plugin.getConfig().getBoolean("xray.automatic-alert")) {
              activeXrayTimers.remove(targetUUID);
              this.cancel();
              return;
            }

            boolean enabled = plugin.getConfig().getBoolean("notes.alert-for-noted-players.enabled");
            boolean alertXrayCheck = plugin.getConfig().getBoolean("notes.alert-for-noted-players.alert-on-xray-suspicion");

            for (Player onlineStaff : Bukkit.getOnlinePlayers()) {
              if (onlineStaff.hasPermission("apexstaff.admin")) {
                manager.sendStaffAlert(target, onlineStaff);
              }
            }

            if (enabled && alertXrayCheck) {
              int currentNotes = databaseManager.getNoteCount(targetUUID);
              String currentStaffUUID = databaseManager.getStaffUUID(targetUUID);
              int minNotes = plugin.getConfig().getInt("notes.alert-for-noted-players.min-notes-threshold");

              if (currentNotes >= minNotes && currentStaffUUID != null) {
                try {
                  UUID sUUID = UUID.fromString(currentStaffUUID);
                  Player assignedStaff = Bukkit.getPlayer(sUUID);
                  if (assignedStaff != null && assignedStaff.isOnline()) {
                    String alertMessage = MessageUtils.getMessage(plugin, "xray-check-alert",
                            "{target}", target.getName(),
                            "{number_of_notes}", String.valueOf(currentNotes)
                    );
                    if (alertMessage != null) assignedStaff.sendMessage(alertMessage);
                  }
                } catch (IllegalArgumentException ignored) {}
              }
            }
          }
        }.runTaskTimer(plugin, 0L, ticks);
      }
    }
  }
}