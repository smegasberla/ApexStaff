package me.smegasberla.apexStaff.hook;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.smegasberla.apexStaff.ApexStaff;
import me.smegasberla.apexStaff.managers.VanishManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlaceholderAPIHook extends PlaceholderExpansion {

  private final ApexStaff plugin;

  public PlaceholderAPIHook(ApexStaff plugin) {
    this.plugin = plugin;
  }

  public void registerHook() {
    if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
      this.register();
      plugin.getLogger().info("Successfully linked with PlaceholderAPI!");
    } else {
      plugin.getLogger().warning("PlaceholderAPI not found! Placeholders will not work.");
    }
  }


  @Override
  public @NotNull String getIdentifier() {
    return "apexstaff";
  }

  @Override
  public @NotNull String getAuthor() {
    return "smegasberla";
  }

  @Override
  public @NotNull String getVersion() {
    return plugin.getDescription().getVersion();
  }

  @Override
  public boolean persist() {
    return true;
  }


  @Override
  public String onRequest(OfflinePlayer offlinePlayer, @NotNull String params) {

    Player player = offlinePlayer.getPlayer();

    if (player == null)
      return "";

    if (params.equalsIgnoreCase("online_staff")) {

      return plugin.getPlaceholderManager().getOnlineStaffCount();
    }

    if (params.equalsIgnoreCase("status")) {

      return plugin.getPlaceholderManager().getStatus(player);
    }

    if (params.equalsIgnoreCase("vanished")) {

      return plugin.getPlaceholderManager().isVanished(player);
    }

    if (params.equalsIgnoreCase("fly_status")) {

      return plugin.getPlaceholderManager().getFlyStatus(player);

    }

    if (params.equalsIgnoreCase("ping")) {

      return plugin.getPlaceholderManager().getPing(player);

    }

    if (params.equalsIgnoreCase("colored_ping")) {

      return plugin.getPlaceholderManager().getColoredPing(player);

    }

    if (params.equalsIgnoreCase("tps")) {

      return plugin.getPlaceholderManager().getFormattedTPS();

    }

    if (params.equalsIgnoreCase("total_ores")) {

      return plugin.getPlaceholderManager().totalOresBlocks(player);

    }

    if (params.equalsIgnoreCase("ore_block_ratio")) {

      return plugin.getPlaceholderManager().getOreBlockRatio(player);

    }

    if (params.equalsIgnoreCase("vanish_icon")) {

      return plugin.getPlaceholderManager().getVanishIcon(player);

    }

    return null;

  }

  

}
