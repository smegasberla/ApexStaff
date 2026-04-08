package me.smegasberla.exstension.api.manager;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class PlaceholderManager {

    private final JavaPlugin plugin;
    private final Map<String, PlaceholderExpansion> expansions;
    private final Map<String, Function<Player, String>> placeholders;

    public PlaceholderManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.expansions = new HashMap<>();
        this.placeholders = new HashMap<>();
    }

    public boolean isPlaceholderAPIEnabled() {
        return Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
    }

    public void registerExpansion(String identifier, String author, String version) {
        if (!isPlaceholderAPIEnabled()) {
            return;
        }

        PlaceholderExpansion expansion = new PlaceholderExpansion() {
            @Override
            public String getIdentifier() {
                return identifier;
            }

            @Override
            public String getAuthor() {
                return author;
            }

            @Override
            public String getVersion() {
                return version;
            }



            @Override
            public String onPlaceholderRequest(Player player, String params) {
                if (placeholders.containsKey(params)) {
                    return placeholders.get(params).apply(player);
                }
                return null;
            }
        };

        if (expansion.register()) {
            expansions.put(identifier, expansion);
        }
    }

    public void registerPlaceholder(String key, Function<Player, String> function) {
        placeholders.put(key, function);
    }

    public void unregisterExpansion(String identifier) {
        PlaceholderExpansion expansion = expansions.remove(identifier);
        if (expansion != null) {
            expansion.unregister();
        }
    }

    public void unregisterAll() {
        for (PlaceholderExpansion expansion : expansions.values()) {
            expansion.unregister();
        }
        expansions.clear();
        placeholders.clear();
    }

    public String setPlaceholders(Player player, String text) {
        if (isPlaceholderAPIEnabled()) {
            return PlaceholderAPI.setPlaceholders(player, text);
        }
        return text;
    }

    public boolean hasExpansion(String identifier) {
        return expansions.containsKey(identifier);
    }

    public int getExpansionCount() {
        return expansions.size();
    }
}
