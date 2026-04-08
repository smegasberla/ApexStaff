package me.smegasberla.exstension.api.manager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LocationManager {

    private final JavaPlugin plugin;
    private final Map<String, Location> savedLocations;
    private File locationsFile;
    private FileConfiguration locationsConfig;

    public LocationManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.savedLocations = new HashMap<>();
    }

    public void init() {
        locationsFile = new File(plugin.getDataFolder(), "locations.yml");
        if (!locationsFile.exists()) {
            locationsFile.getParentFile().mkdirs();
            try {
                locationsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        locationsConfig = YamlConfiguration.loadConfiguration(locationsFile);
    }

    public void saveLocation(String name, Location location) {
        savedLocations.put(name, location);
        locationsConfig.set(name + ".world", location.getWorld().getName());
        locationsConfig.set(name + ".x", location.getX());
        locationsConfig.set(name + ".y", location.getY());
        locationsConfig.set(name + ".z", location.getZ());
        locationsConfig.set(name + ".yaw", location.getYaw());
        locationsConfig.set(name + ".pitch", location.getPitch());
        saveLocations();
    }

    public void saveLocations() {
        try {
            locationsConfig.save(locationsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Location getLocation(String name) {
        if (locationsConfig.contains(name)) {
            String world = locationsConfig.getString(name + ".world");
            double x = locationsConfig.getDouble(name + ".x");
            double y = locationsConfig.getDouble(name + ".y");
            double z = locationsConfig.getDouble(name + ".z");
            float yaw = (float) locationsConfig.getDouble(name + ".yaw");
            float pitch = (float) locationsConfig.getDouble(name + ".pitch");
            return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
        }
        return null;
    }

    public void deleteLocation(String name) {
        savedLocations.remove(name);
        locationsConfig.set(name, null);
        saveLocations();
    }

    public boolean hasLocation(String name) {
        return locationsConfig.contains(name);
    }

    public Map<String, Location> getAllLocations() {
        return new HashMap<>(savedLocations);
    }

    public void clearAllLocations() {
        savedLocations.clear();
        locationsConfig = new YamlConfiguration();
        saveLocations();
    }

    public Location createLocation(String worldName, double x, double y, double z, float yaw, float pitch) {
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            world = Bukkit.getWorlds().get(0);
        }
        return new Location(world, x, y, z, yaw, pitch);
    }

    public Location teleport(Player player, Location location) {
        player.teleport(location);
        return location;
    }

    public Location getSafeLocation(Location location) {
        Location safeLocation = location.clone();
        while (safeLocation.getBlock().getType().isSolid()) {
            safeLocation.setY(safeLocation.getY() + 1);
        }
        while (!safeLocation.clone().subtract(0, 1, 0).getBlock().getType().isSolid()) {
            safeLocation.setY(safeLocation.getY() - 1);
            if (safeLocation.getY() < 0) {
                break;
            }
        }
        return safeLocation;
    }
}
