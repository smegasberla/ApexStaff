package me.smegasberla.apexStaff;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import me.smegasberla.apexStaff.commands.ApexCommand;
import me.smegasberla.apexStaff.hook.PlaceholderAPIHook;
import me.smegasberla.apexStaff.listeners.*;
import me.smegasberla.apexStaff.listeners.packet.ShadowCamListener;
import me.smegasberla.apexStaff.managers.*;
import me.smegasberla.apexStaff.models.FreezeModel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

import static me.smegasberla.apexStaff.managers.DatabaseManager.addXrayData;

public final class ApexStaff extends JavaPlugin {

  private static ApexStaff plugin;
  private XRayCheckManager sharedManager = new XRayCheckManager(this);
  private DatabaseManager databaseManager = new DatabaseManager(this);
  private FlyManager flyManager = new FlyManager();
  private DupeIPManager dupeIPManager = new DupeIPManager();
  private ShadowCamManager shadowCamManager = new ShadowCamManager();
  private VanishManager vanishManager = new VanishManager();
  private FreezeManager freezeManager = new FreezeManager();
  private PlaceholderManager placeholderManager = new PlaceholderManager(this, vanishManager);
  private PlaceholderAPIHook papiHook;

  @Override
  public void onLoad() {

    PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
    PacketEvents.getAPI().getSettings()
        .reEncodeByDefault(true)
        .bStats(false)
        .checkForUpdates(true);

    PacketEvents.getAPI().load();

  }

  @Override
  public void onEnable() {

    plugin = this;

    this.papiHook = new PlaceholderAPIHook(this);
    this.papiHook.registerHook();

    this.sharedManager = new XRayCheckManager(this);
    this.databaseManager = new DatabaseManager(this);
    this.flyManager = new FlyManager();
    this.dupeIPManager = new DupeIPManager();
    this.shadowCamManager = new ShadowCamManager();
    this.freezeManager = new FreezeManager();
    this.vanishManager = new VanishManager();
    this.placeholderManager = new PlaceholderManager(this, vanishManager);

    getConfig().options().copyDefaults(true);
    saveDefaultConfig();

    PacketEvents.getAPI().init();

    getCommand("apexstaff").setExecutor(new ApexCommand(sharedManager, flyManager, databaseManager, shadowCamManager));

    getServer().getPluginManager().registerEvents(new MovementListener(this), this);
    getServer().getPluginManager().registerEvents(new DamageListener(this), this);
    getServer().getPluginManager().registerEvents(new ChatListener(this), this);
    getServer().getPluginManager().registerEvents(new QuitListener(this, databaseManager, sharedManager, dupeIPManager),
        this);
    getServer().getPluginManager().registerEvents(new AsyncPreLoginListener(this, databaseManager, sharedManager),
        this);
    getServer().getPluginManager().registerEvents(new BlockBreakListener(this, databaseManager,sharedManager), this);
    getServer().getPluginManager().registerEvents(new JoinListener(this,this, dupeIPManager, databaseManager), this);

    PacketEvents.getAPI().getEventManager().registerListener(new ShadowCamListener(this, shadowCamManager),
        PacketListenerPriority.NORMAL);

    if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {

      papiHook.registerHook();

    }

    DatabaseManager.init();
    DatabaseManager.createTables();

  }

  @Override
  public void onDisable() {

    DatabaseManager.closeConnection();
    PacketEvents.getAPI().terminate();

  }

  public PlaceholderManager getPlaceholderManager() {
    return placeholderManager;
  }

  public PlaceholderAPIHook getPapiHook() {
    return papiHook;
  }

  public XRayCheckManager getXrayCheckManager() {
    return sharedManager;
  }

  public DatabaseManager getDatabaseManager() {
    return databaseManager;
  }

  public FlyManager getFlyManager() {
    return flyManager;
  }

  public DupeIPManager getDupeIPManager() {
    return dupeIPManager;
  }

  public ShadowCamManager getShadowCamManager() {
    return shadowCamManager;
  }

  public VanishManager getVanishManager() {
    return vanishManager;
  }

  public FreezeManager getFreezeManager() {
    return freezeManager;
  }

  public static void sendHelpMessage(CommandSender sender) {
   sender.sendMessage(ChatColor.DARK_GRAY + "=================================");
   sender.sendMessage(ChatColor.GOLD + "        ApexStaff Help Menu");
   sender.sendMessage(ChatColor.DARK_GRAY + "=================================");
   sender.sendMessage(ChatColor.YELLOW + "  Moderation:");
   sender.sendMessage(ChatColor.AQUA + "  /apexstaff freeze <player>" + ChatColor.GRAY + " - Freeze a player");
   sender.sendMessage(ChatColor.AQUA + "  /apexstaff vanish <player>" + ChatColor.GRAY + " - Toggle vanish mode");
   sender.sendMessage(ChatColor.AQUA + "  /apexstaff clearchat <player>" + ChatColor.GRAY + " - Clear server chat");
   sender.sendMessage(ChatColor.YELLOW + "  Utilities:");
   sender.sendMessage(ChatColor.AQUA + "  /apexstaff fly <player>" + ChatColor.GRAY + " - Toggle flight");
   sender.sendMessage(ChatColor.AQUA + "  /apexstaff ping <player>" + ChatColor.GRAY + " - Check ping");
   sender.sendMessage(ChatColor.AQUA + "  /apexstaff shadowcam <player>" + ChatColor.GRAY + " - Toggle shadow cam");
   sender.sendMessage(ChatColor.AQUA + "  /apexstaff notes <player> [add|list|remove|clear]" + ChatColor.GRAY + " - Manage player notes");
   sender.sendMessage(ChatColor.YELLOW + "  Detection:");
   sender.sendMessage(ChatColor.AQUA + "  /apexstaff xray <player> [info|clear]" + ChatColor.GRAY + " - X-Ray checks");
   sender.sendMessage(ChatColor.AQUA + "  /apexstaff dupeip <player>" + ChatColor.GRAY + " - Check duplicate IPs");
   sender.sendMessage(ChatColor.YELLOW + "  System:");
   sender.sendMessage(ChatColor.AQUA + "  /apexstaff reload" + ChatColor.GRAY + " - Reload configuration");
   sender.sendMessage(ChatColor.DARK_GRAY + "=================================");
 }

  public static ApexStaff getPlugin() {
    return plugin;
  }

  public void startCleanupTask() {

    Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {

      int deleted = DatabaseManager.cleanupExpiredBans();

      java.util.List<java.util.UUID> toRemove = new java.util.ArrayList<>();

      for (java.util.UUID uuid : FreezeModel.getAllBannedUUIDs()) {
        FreezeModel model = FreezeModel.getBan(uuid);
        if (model != null && model.isExpired()) {
          toRemove.add(uuid);
        }
      }

      for (java.util.UUID uuid : toRemove) {
        FreezeModel.removeBan(uuid);
      }

    }, 20L * 60, 20L * 60 * 5);
  }

  public void startSaveXrayDataStats() {

    Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {

      for (Player player : Bukkit.getOnlinePlayers()) {
        UUID uuid = player.getUniqueId();

        if (sharedManager.totalBlocks.containsKey(uuid)) {
          int blocks = sharedManager.totalBlocks.get(uuid);
          int ores = sharedManager.totalOres.get(uuid);

          addXrayData(player, blocks, ores);
        }
      }

    }, 20L * 60 * 5, 20L * 60 * 5);

  }


}
