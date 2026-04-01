package me.smegasberla.apexStaff.managers;

import me.smegasberla.apexStaff.ApexStaff;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PlaceholderManager {

  private final ApexStaff plugin;
  private final VanishManager vanishManager;

  public PlaceholderManager(ApexStaff plugin, VanishManager vanishManager) {
    this.plugin = plugin;
    this.vanishManager = vanishManager;
  }

  public String getOnlineStaffCount() {
    long count = Bukkit.getOnlinePlayers().stream()
            .filter(p -> p.hasPermission("apexstaff.admin"))
            .count();
    return String.valueOf(count);
  }

  public String getStatus(OfflinePlayer p) {
    Player online = p.getPlayer();
    if (online != null && online.hasPermission("apexstaff.admin")) {
      return ChatColor.GREEN + "Staff Mode";
    }
    return ChatColor.RED + "Player Mode";
  }

  public String isVanished(OfflinePlayer p) {
    if (vanishManager.vanishedPlayers.contains(p.getUniqueId())) {
      return "true";
    }
    return "false";
  }

  public String isFrozen(OfflinePlayer p) {
    if (plugin.getFreezeManager().freezedPlayers.contains(p.getUniqueId())) {
      return ChatColor.AQUA + "Frozen";
    }
    return ChatColor.GREEN + "Free";
  }

  public String getFlyStatus(OfflinePlayer p) {
    if (plugin.getFlyManager().customFlight.contains(p.getUniqueId())) {
      return ChatColor.GREEN + "Flight Enabled";
    }
    return ChatColor.YELLOW + "Flight Disabled";
  }

  public String getPing(OfflinePlayer p) {
    Player online = p.getPlayer();
    if (online == null) return "0";
    return String.valueOf(online.getPing());
  }

  public String getColoredPing(OfflinePlayer p) {
    Player online = p.getPlayer();
    if (online == null) return ChatColor.GRAY + "Offline";

    int ping = online.getPing();
    ChatColor color;
    if (ping < 50) color = ChatColor.GREEN;
    else if (ping < 150) color = ChatColor.YELLOW;
    else color = ChatColor.RED;

    return "Your current ping is: " + color + ping;
  }

  public String getFormattedTPS() {
    double[] tps = Bukkit.getTPS();
    double oneMinute = Math.min(20.0, tps[0]);
    return String.format("%.2f", oneMinute);
  }

  public String totalOresBlocks(OfflinePlayer p) {
    int totalOresMined = plugin.getXrayCheckManager().totalOres.getOrDefault(p.getUniqueId(), 0);
    return String.valueOf(totalOresMined);
  }

  public String getOreBlockRatio(OfflinePlayer p) {
    Player online = p.getPlayer();
    if (online == null) {
      return "0.00%";
    }

    double ratio = plugin.getXrayCheckManager().calcutaleXRayPercentage(online);

    if (Double.isNaN(ratio) || Double.isInfinite(ratio)) {
      return "0.00%";
    }

    return String.format("%.2f%%", ratio);
  }

  public String getVanishIcon(OfflinePlayer p) {
    if (vanishManager.vanishedPlayers.contains(p.getUniqueId())) {
      return ChatColor.GREEN + "[V]";
    }
    return ChatColor.AQUA + "[O]";
  }
}