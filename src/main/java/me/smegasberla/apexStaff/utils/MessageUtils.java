package me.smegasberla.apexStaff.utils;

import me.smegasberla.apexStaff.ApexStaff;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageUtils {

    private static final Pattern IP_PATTERN = Pattern.compile(
            "\\b(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b"
    );

    public static String censorIPs(String message) {
        if (message == null || message.isEmpty()) return message;
        Matcher matcher = IP_PATTERN.matcher(message);
        return matcher.replaceAll("***.***.***.***");
    }

    public static boolean containsIP(String message) {
        if (message == null || message.isEmpty()) return false;
        return IP_PATTERN.matcher(message).find();
    }

    public static String translateColorCodes(String message) {
        if (message == null || message.isEmpty()) return message;
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String stripColorCodes(String message) {
        if (message == null || message.isEmpty()) return message;
        return ChatColor.stripColor(message);
    }

    public static String getMessage(ApexStaff plugin, String path) {
        String message = plugin.getConfig().getString(path);

        if (message == null || message.isEmpty()) {
            plugin.getLogger().warning("[Config Error] Missing path: " + path);
            return ChatColor.RED + "(Missing Config: " + path + ")";
        }

        return translateColorCodes(message);
    }

    public static String getMessage(ApexStaff plugin, String path, String... replacements) {
        String message = plugin.getConfig().getString(path);

        if (message == null || message.isEmpty()) {
            plugin.getLogger().warning("[Config Error] Missing path: " + path);
            return ChatColor.RED + "(Missing Config: " + path + ")";
        }

        if (replacements.length > 0 && replacements.length % 2 == 0) {
            for (int i = 0; i < replacements.length; i += 2) {
                message = message.replace(replacements[i], replacements[i + 1]);
            }
        }

        return translateColorCodes(message);
    }
}