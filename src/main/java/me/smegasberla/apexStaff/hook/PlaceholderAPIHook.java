package me.smegasberla.apexStaff.hook;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.smegasberla.apexStaff.ApexStaff;
import me.smegasberla.apexStaff.managers.VanishManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlaceholderAPIHook extends PlaceholderExpansion {

    private final ApexStaff plugin;


    public PlaceholderAPIHook(ApexStaff plugin) {
        this.plugin = plugin;
    }


    public void registerHook() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            this.register();
            plugin.getLogger().info("Successfully linked with PlaceholderAPI!");
        }
    }

    // --- Expansion Settings ---

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
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) return "";

       //GENERAL PURPOSE PLACEHOLDERS

        if (params.equalsIgnoreCase("online_staff")) {

            return plugin.getPlaceholderManager().getOnlineStaffCount();
        }



        return null;
    }

    public static String format(Player player, String message) {
        return format(player, null, message);
    }


    public static String format(Player player, Player target, String message) {
        if (message == null || message.isEmpty()) return "";


        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            message = PlaceholderAPI.setPlaceholders(player, message);
        }

        if (message.contains("{online_staff}")) {
            long count = Bukkit.getOnlinePlayers().stream()
                    .filter(p -> p.hasPermission("apexstaff.admin"))
                    .count();
            message = message.replace("{online_staff}", String.valueOf(count));
        }


        if (player != null) {
            message = message.replace("{player}", player.getName())
                    .replace("{displayname}", player.getDisplayName())
                    .replace("{uuid}", player.getUniqueId().toString())
                    .replace("{ip}", player.getAddress().getHostString())
                    .replace("{ping}", String.valueOf(player.getPing()));

        }


        if (target != null) {
            message = message.replace("{target}", target.getName())
                    .replace("{target_displayname}", target.getDisplayName())
                    .replace("{target_uuid}", target.getUniqueId().toString())
                    .replace("{target_ip}", player.getAddress().getHostString())
                    .replace("{target_ping}", String.valueOf(target.getPing()));
        }

        message = message.replace("{prefix}", "§8[§bApex§8] ");


        return ChatColor.translateAlternateColorCodes('&', message);
    }
}

