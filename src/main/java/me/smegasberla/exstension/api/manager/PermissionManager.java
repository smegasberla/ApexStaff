package me.smegasberla.exstension.api.manager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermissionManager {

    private final JavaPlugin plugin;
    private final Map<String, Permission> registeredPermissions;

    public PermissionManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.registeredPermissions = new HashMap<>();
    }

    public Permission registerPermission(String name) {
        return registerPermission(name, PermissionDefault.OP);
    }

    public Permission registerPermission(String name, PermissionDefault defaultValue) {
        Permission permission = new Permission(name, "Permission for " + name, defaultValue);
        registeredPermissions.put(name, permission);
        plugin.getServer().getPluginManager().addPermission(permission);
        return permission;
    }

    public Permission registerPermission(String name, String description) {
        return registerPermission(name, description, PermissionDefault.OP);
    }

    public Permission registerPermission(String name, String description, PermissionDefault defaultValue) {
        Permission permission = new Permission(name, description, defaultValue);
        registeredPermissions.put(name, permission);
        plugin.getServer().getPluginManager().addPermission(permission);
        return permission;
    }

    public boolean hasPermission(Player player, String permission) {
        return player.hasPermission(permission);
    }

    public boolean hasPermission(Player player, Permission permission) {
        return player.hasPermission(permission);
    }

    public void addPermission(Player player, String permission) {
        player.addAttachment(plugin, permission, true);
    }

    public void removePermission(Player player, String permission) {
        player.addAttachment(plugin, permission, false);
    }

    public void unregisterPermission(String name) {
        Permission permission = registeredPermissions.remove(name);
        if (permission != null) {
            plugin.getServer().getPluginManager().removePermission(permission);
        }
    }

    public void unregisterAll() {
        for (Permission permission : registeredPermissions.values()) {
            plugin.getServer().getPluginManager().removePermission(permission);
        }
        registeredPermissions.clear();
    }

    public boolean isRegistered(String name) {
        return registeredPermissions.containsKey(name);
    }

    public int getPermissionCount() {
        return registeredPermissions.size();
    }

    public List<String> getRegisteredPermissions() {
        return new ArrayList<>(registeredPermissions.keySet());
    }
}
