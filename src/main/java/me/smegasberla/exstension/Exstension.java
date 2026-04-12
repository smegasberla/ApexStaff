package me.smegasberla.exstension;

import me.smegasberla.apexStaff.ApexStaff;
import me.smegasberla.exstension.api.ApexStaffExstension;
import me.smegasberla.exstension.api.ExstensionAPI;
import me.smegasberla.exstension.api.manager.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public abstract class Exstension implements ApexStaffExstension {

    protected JavaPlugin plugin;
    protected ExstensionAPI api;
    protected ExstensionConfig config;
    protected ExstensionLogger logger;
    private File dataFolder;
    private File file;

    public final void init(JavaPlugin plugin, File dataFolder, File file) {
        this.plugin = plugin;
        this.dataFolder = dataFolder;
        this.file = file;
        this.api = new ExstensionAPI(plugin);
        this.logger = new ExstensionLogger(getName());
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

    @Override
    public String getName() {
        return "Exstension";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public ApexStaff getApexStaff() {
        return ApexStaff.getPlugin();
    }

    public ExstensionAPI getAPI() {
        return api;
    }

    public CommandManager getCommandManager() {
        return api.getCommandManager();
    }

    public GUIManager getGuiManager() {
        return api.getGuiManager();
    }

    public EventManager getEventManager() {
        return api.getEventManager();
    }

    public TaskManager getTaskManager() {
        return api.getTaskManager();
    }

    public ScoreboardManager getScoreboardManager() {
        return api.getScoreboardManager();
    }

    public PermissionManager getPermissionManager() {
        return api.getPermissionManager();
    }

    public MessageManager getMessageManager() {
        return api.getMessageManager();
    }

    public ItemManager getItemManager() {
        return api.getItemManager();
    }

    public PlayerManager getPlayerManager() {
        return api.getPlayerManager();
    }

    public WorldManager getWorldManager() {
        return api.getWorldManager();
    }

    public LocationManager getLocationManager() {
        return api.getLocationManager();
    }

    public ConfigManager getConfigManager() {
        return api.getConfigManager();
    }

    public NMSManager getNMSManager() {
        return api.getNMSManager();
    }

    public MetricsManager getMetricsManager(String resourceId) {
        return api.getMetricsManager(resourceId);
    }

    public UpdateManager getUpdateManager(int resourceId, String version) {
        return api.getUpdateManager(resourceId, version);
    }

    public DatabaseManager getDatabaseManager(String host, String database, String username, String password) {
        return api.getDatabaseManager(host, database, username, password);
    }

    public me.smegasberla.exstension.api.manager.PlaceholderManager getPlaceholderManager() {
        return api.getPlaceholderManager();
    }   

    protected ExstensionLogger getLogger() {
        return logger;
    }

    public File getDataFolder() {
        return dataFolder;
    }

    protected File getFile() {
        return file;
    }

    protected void saveDefaultConfig() {
        if (config == null) {
            File configFile = new File(dataFolder, "config.yml");
            if (!configFile.exists()) {
                plugin.saveResource("config.yml", false);
            }
            config = new ExstensionConfig(configFile);
            config.load();
        }
    }

    protected ExstensionConfig getConfig() {
        return config;
    }

    protected void reloadConfig() {
        if (config != null) {
            config.load();
        }
    }

    protected void saveConfig() {
        if (config != null) {
            config.save();
        }
    }
}
