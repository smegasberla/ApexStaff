package me.smegasberla.exstension.api.manager;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPOutputStream;

public class MetricsManager {

    private final JavaPlugin plugin;
    private final String resourceId;
    private final ScheduledExecutorService scheduler;
    private final Map<String, Object> customFields;

    public MetricsManager(JavaPlugin plugin, String resourceId) {
        this.plugin = plugin;
        this.resourceId = resourceId;
        this.customFields = new HashMap<>();
        this.scheduler = Executors.newScheduledThreadPool(1);
    }

    public void startLogging() {
        scheduler.scheduleAtFixedRate(this::submitMetrics, 0, 30, TimeUnit.MINUTES);
    }

    private void submitMetrics() {
        try {
            String serverVersion = Bukkit.getVersion();
            String javaVersion = System.getProperty("java.version");
            int playerCount = Bukkit.getOnlinePlayers().size();
            int maxPlayers = Bukkit.getMaxPlayers();

            Map<String, Object> data = new HashMap<>();
            data.put("serverVersion", serverVersion);
            data.put("javaVersion", javaVersion);
            data.put("playerCount", playerCount);
            data.put("maxPlayers", maxPlayers);
            data.put("uuid", UUID.randomUUID().toString());
            data.put("resourceId", resourceId);

            for (Map.Entry<String, Object> entry : customFields.entrySet()) {
                data.put(entry.getKey(), entry.getValue());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addCustomField(String key, Object value) {
        customFields.put(key, value);
    }

    public void removeCustomField(String key) {
        customFields.remove(key);
    }

    public void stop() {
        scheduler.shutdown();
    }

    public int getPlayerCount() {
        return Bukkit.getOnlinePlayers().size();
    }

    public int getMaxPlayers() {
        return Bukkit.getMaxPlayers();
    }

    public String getServerVersion() {
        return Bukkit.getVersion();
    }
}
