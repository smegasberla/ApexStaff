package me.smegasberla.apexStaff.listeners;

import me.smegasberla.apexStaff.ApexStaff;
import me.smegasberla.apexStaff.enums.MiningBlock;
import me.smegasberla.apexStaff.managers.XRayCheckManager;
import me.smegasberla.apexStaff.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.UUID;

import static org.bukkit.Bukkit.getLogger;

public class BlockBreakListener implements Listener {

  private final ApexStaff plugin;

  public BlockBreakListener(ApexStaff plugin, XRayCheckManager manager) {
    this.plugin = plugin;
    this.manager = manager;
  }

  private final XRayCheckManager manager;

  @EventHandler
  public void onPlayerBreak(BlockBreakEvent e) {

    if (plugin.getConfig().getBoolean("xray.enabled") == true) {

      List<String> materialList = manager.getMaterialList();

      Player p = e.getPlayer();

      double alertThreshold = plugin.getConfig().getDouble("xray.alert-threshold");
      double percentage = plugin.getXrayCheckManager().calcutaleXRayPercentage(p);

      UUID playerUUID = p.getUniqueId();

      if (p.hasPermission("apexstaff.exempt"))
        return;

      Material blockMaterial = e.getBlock().getType();

      if (MiningBlock.isMiningBlock(blockMaterial)) {

        int currentCount = manager.totalBlocks.getOrDefault(playerUUID, 0);
        manager.totalBlocks.put(playerUUID, currentCount + 1);

      }

      if (materialList.contains(blockMaterial.toString())) {

        int currentOreCount = manager.totalOres.getOrDefault(playerUUID, 0);
        manager.totalOres.put(playerUUID, currentOreCount + 1);

      }

      if (percentage > alertThreshold && plugin.getXrayCheckManager().isSuspect(p)) {

        boolean automaticAlert = plugin.getConfig().getBoolean("xray.automatic-alert");

        if(automaticAlert == true) {

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

              if (Bukkit.getOnlinePlayers().isEmpty()) return;

              XRayCheckManager manager = plugin.getXrayCheckManager();

              for (Player staff : Bukkit.getOnlinePlayers()) {
                if (staff.hasPermission("apexstaff.admin")) {

                  manager.sendStaffAlert(p, staff);
                }
              }
            }
          }.runTaskTimer(plugin, ticks, ticks);

        }

      }


    }

  }
}