package me.smegasberla.exstension.api.manager;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class MessageManager {

    private final JavaPlugin plugin;
    private String prefix;

    public MessageManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.prefix = "";
    }

    public MessageManager(JavaPlugin plugin, String prefix) {
        this.plugin = plugin;
        this.prefix = prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }

    public String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', prefix + message);
    }

    public String decolorize(String message) {
        return ChatColor.stripColor(colorize(message));
    }

    public void sendMessage(CommandSender sender, String message) {
        if (sender != null) {
            sender.sendMessage(colorize(message));
        }
    }

    public void sendMessage(Player player, String message) {
        sendMessage((CommandSender) player, message);
    }

    public void broadcastMessage(String message) {
        Bukkit.broadcastMessage(colorize(message));
    }

    public void broadcastToStaff(String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("exstension.admin")) {
                sendMessage(player, message);
            }
        }
    }

    public void sendMessages(CommandSender sender, List<String> messages) {
        for (String message : messages) {
            sendMessage(sender, message);
        }
    }

    public void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        player.sendTitle(
            colorize(title),
            colorize(subtitle),
            fadeIn,
            stay,
            fadeOut
        );
    }

    public void sendTitle(Player player, String title, String subtitle) {
        sendTitle(player, title, subtitle, 10, 70, 20);
    }

    public void sendActionBar(Player player, String message) {
        player.sendActionBar(colorize(message));
    }

    public void playSound(Player player, org.bukkit.Sound sound, float volume, float pitch) {
        player.playSound(player.getLocation(), sound, volume, pitch);
    }

    public void kickPlayer(Player player, String reason) {
        player.kickPlayer(colorize(reason));
    }
}
