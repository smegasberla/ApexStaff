package me.smegasberla.exstension.api.manager;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class PlayerManager {

    private final JavaPlugin plugin;

    public PlayerManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public Player getPlayer(String name) {
        return Bukkit.getPlayer(name);
    }

    public Player getPlayer(UUID uuid) {
        return Bukkit.getPlayer(uuid);
    }

    public OfflinePlayer getOfflinePlayer(String name) {
        return Bukkit.getOfflinePlayer(name);
    }

    public OfflinePlayer getOfflinePlayer(UUID uuid) {
        return Bukkit.getOfflinePlayer(uuid);
    }

    public List<Player> getOnlinePlayers() {
        return new ArrayList<>(Bukkit.getOnlinePlayers());
    }

    public int getOnlinePlayerCount() {
        return Bukkit.getOnlinePlayers().size();
    }

    public int getMaxPlayers() {
        return Bukkit.getMaxPlayers();
    }

    public boolean isOnline(String name) {
        return getPlayer(name) != null;
    }

    public boolean isOnline(UUID uuid) {
        return getPlayer(uuid) != null;
    }

    public void forEachOnlinePlayer(Consumer<Player> action) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            action.accept(player);
        }
    }

    public List<Player> getPlayersWithPermission(String permission) {
        List<Player> players = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission(permission)) {
                players.add(player);
            }
        }
        return players;
    }

    public List<Player> getPlayersInWorld(String worldName) {
        List<Player> players = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getWorld().getName().equals(worldName)) {
                players.add(player);
            }
        }
        return players;
    }

    public void broadcastMessage(String message) {
        Bukkit.broadcastMessage(message);
    }

    public void broadcastMessageToStaff(String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("exstension.admin")) {
                player.sendMessage(message);
            }
        }
    }

    public void kickAll(String reason) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.kickPlayer(reason);
        }
    }

    public boolean hasPlayers() {
        return !Bukkit.getOnlinePlayers().isEmpty();
    }

    public UUID getUUID(String name) {
        OfflinePlayer offlinePlayer = getOfflinePlayer(name);
        if (offlinePlayer != null) {
            return offlinePlayer.getUniqueId();
        }
        return null;
    }

    public String getName(UUID uuid) {
        OfflinePlayer offlinePlayer = getOfflinePlayer(uuid);
        if (offlinePlayer != null) {
            return offlinePlayer.getName();
        }
        return null;
    }
}
