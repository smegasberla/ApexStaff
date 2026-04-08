package me.smegasberla.exstension.api.manager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class WorldManager {

    private final JavaPlugin plugin;

    public WorldManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public World getWorld(String name) {
        return Bukkit.getWorld(name);
    }

    public World getWorld(int index) {
        List<World> worlds = Bukkit.getWorlds();
        if (index >= 0 && index < worlds.size()) {
            return worlds.get(index);
        }
        return null;
    }

    public List<World> getWorlds() {
        return Bukkit.getWorlds();
    }

    public World createWorld(String name, World.Environment environment) {
        WorldCreator creator = new WorldCreator(name);
        creator.environment(environment);
        return creator.createWorld();
    }

    public World createWorld(String name) {
        WorldCreator creator = new WorldCreator(name);
        return creator.createWorld();
    }

    public boolean unloadWorld(String name, boolean save) {
        return Bukkit.unloadWorld(name, save);
    }

    public boolean worldExists(String name) {
        return getWorld(name) != null;
    }

    public List<Entity> getEntities(World world, EntityType type) {
        List<Entity> entities = new ArrayList<>();
        for (Entity entity : world.getEntities()) {
            if (entity.getType() == type) {
                entities.add(entity);
            }
        }
        return entities;
    }

    public int getEntityCount(World world) {
        return world.getEntities().size();
    }

    public int getChunkCount(World world) {
        return world.getChunkCount();
    }

    public void clearEntities(World world, EntityType type) {
        for (Entity entity : getEntities(world, type)) {
            entity.remove();
        }
    }

    public void clearAllEntities(World world) {
        for (Entity entity : world.getEntities()) {
            entity.remove();
        }
    }

    public Location getSpawnLocation(World world) {
        return world.getSpawnLocation();
    }

    public void setSpawnLocation(World world, Location location) {
        world.setSpawnLocation(location);
    }

    public boolean isDayTime(World world) {
        long time = world.getTime();
        return time > 0 && time < 12000;
    }

    public void setDayTime(World world) {
        world.setTime(6000);
        world.setStorm(false);
        world.setThundering(false);
    }

    public void setNightTime(World world) {
        world.setTime(18000);
    }

    public void stopWeather(World world) {
        world.setStorm(false);
        world.setThundering(false);
    }

    public void startStorm(World world) {
        world.setStorm(true);
    }

    public void startThunder(World world) {
        world.setThundering(true);
    }
}
