package me.smegasberla.exstension.api;

import me.smegasberla.exstension.api.manager.CommandManager;
import me.smegasberla.exstension.api.manager.ConfigManager;
import me.smegasberla.exstension.api.manager.DatabaseManager;
import me.smegasberla.exstension.api.manager.EventManager;
import me.smegasberla.exstension.api.manager.GUIManager;
import me.smegasberla.exstension.api.manager.ItemManager;
import me.smegasberla.exstension.api.manager.LocationManager;
import me.smegasberla.exstension.api.manager.MessageManager;
import me.smegasberla.exstension.api.manager.MetricsManager;
import me.smegasberla.exstension.api.manager.NMSManager;
import me.smegasberla.exstension.api.manager.PermissionManager;
import me.smegasberla.exstension.api.manager.PlayerManager;
import me.smegasberla.exstension.api.manager.ScoreboardManager;
import me.smegasberla.exstension.api.manager.TaskManager;
import me.smegasberla.exstension.api.manager.UpdateManager;
import me.smegasberla.exstension.api.manager.WorldManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ExstensionAPI {

    private final JavaPlugin plugin;
    private CommandManager commandManager;
    private GUIManager guiManager;
    private EventManager eventManager;
    private TaskManager taskManager;
    private ScoreboardManager scoreboardManager;
    private PermissionManager permissionManager;
    private MessageManager messageManager;
    private ItemManager itemManager;
    private PlayerManager playerManager;
    private WorldManager worldManager;
    private LocationManager locationManager;
    private ConfigManager configManager;
private NMSManager nmsManager;
private MetricsManager metricsManager;
private UpdateManager updateManager;
private me.smegasberla.exstension.api.manager.PlaceholderManager placeholderManager;

    public ExstensionAPI(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public CommandManager getCommandManager() {
        if (commandManager == null) {
            commandManager = new CommandManager(plugin);
        }
        return commandManager;
    }

    public GUIManager getGuiManager() {
        if (guiManager == null) {
            guiManager = new GUIManager(plugin);
        }
        return guiManager;
    }

    public EventManager getEventManager() {
        if (eventManager == null) {
            eventManager = new EventManager(plugin);
        }
        return eventManager;
    }

    public TaskManager getTaskManager() {
        if (taskManager == null) {
            taskManager = new TaskManager(plugin);
        }
        return taskManager;
    }

    public ScoreboardManager getScoreboardManager() {
        if (scoreboardManager == null) {
            scoreboardManager = new ScoreboardManager();
        }
        return scoreboardManager;
    }

    public PermissionManager getPermissionManager() {
        if (permissionManager == null) {
            permissionManager = new PermissionManager(plugin);
        }
        return permissionManager;
    }

    public MessageManager getMessageManager() {
        if (messageManager == null) {
            messageManager = new MessageManager(plugin);
        }
        return messageManager;
    }

    public ItemManager getItemManager() {
        if (itemManager == null) {
            itemManager = new ItemManager();
        }
        return itemManager;
    }

    public PlayerManager getPlayerManager() {
        if (playerManager == null) {
            playerManager = new PlayerManager(plugin);
        }
        return playerManager;
    }

    public WorldManager getWorldManager() {
        if (worldManager == null) {
            worldManager = new WorldManager(plugin);
        }
        return worldManager;
    }

    public LocationManager getLocationManager() {
        if (locationManager == null) {
            locationManager = new LocationManager(plugin);
        }
        return locationManager;
    }

    public ConfigManager getConfigManager() {
        if (configManager == null) {
            configManager = new ConfigManager(plugin);
        }
        return configManager;
    }

    public NMSManager getNMSManager() {
        if (nmsManager == null) {
            nmsManager = new NMSManager();
        }
        return nmsManager;
    }

    public MetricsManager getMetricsManager(String resourceId) {
        if (metricsManager == null) {
            metricsManager = new MetricsManager(plugin, resourceId);
        }
        return metricsManager;
    }

    public UpdateManager getUpdateManager(int resourceId, String version) {
        if (updateManager == null) {
            updateManager = new UpdateManager(plugin, resourceId, version);
        }
        return updateManager;
    }

    public DatabaseManager getDatabaseManager(String host, String database, String username, String password) {
        return new DatabaseManager(host, database, username, password);
    }

    public me.smegasberla.exstension.api.manager.PlaceholderManager getPlaceholderManager() {
        if (placeholderManager == null) {
            placeholderManager = new me.smegasberla.exstension.api.manager.PlaceholderManager(plugin);
        }
        return placeholderManager;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }
}
