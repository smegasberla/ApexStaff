package me.smegasberla.exstension.api.manager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.function.Consumer;
import java.util.function.BiConsumer;

public class UpdateManager {

    private final JavaPlugin plugin;
    private final int resourceId;
    private final String currentVersion;
    private String latestVersion;
    private boolean updateAvailable;

    public UpdateManager(JavaPlugin plugin, int resourceId, String currentVersion) {
        this.plugin = plugin;
        this.resourceId = resourceId;
        this.currentVersion = currentVersion;
        this.updateAvailable = false;
    }

    public void checkForUpdate(Consumer<Boolean> callback) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                URL url = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + resourceId);
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
                    latestVersion = reader.readLine();
                    updateAvailable = latestVersion != null && !latestVersion.equals(currentVersion);
                    if (callback != null) {
                        Bukkit.getScheduler().runTask(plugin, () -> callback.accept(updateAvailable));
                    }
                }
            } catch (Exception e) {
                if (callback != null) {
                    Bukkit.getScheduler().runTask(plugin, () -> callback.accept(false));
                }
            }
        });
    }

    public void checkForUpdate() {
        checkForUpdate(null);
    }

    public void notifyOnJoin(Consumer<String> notifyMessage) {
        checkForUpdate(hasUpdate -> {
            if (hasUpdate) {
                String message = notifyMessage != null ? 
                    notifyMessage.toString() : 
                    "&7[&6" + plugin.getName() + "&7] &eUpdate available! &7(&6" + latestVersion + "&7)";
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.hasPermission(plugin.getName().toLowerCase() + ".admin")) {
                        player.sendMessage(message);
                    }
                }
            }
        });
    }

    public boolean isUpdateAvailable() {
        return updateAvailable;
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public String getCurrentVersion() {
        return currentVersion;
    }

    public boolean isUpToDate() {
        return currentVersion.equals(latestVersion);
    }
}
