package me.smegasberla.apexStaff;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerPriority;

import com.github.retrooper.packetevents.protocol.player.User;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import me.clip.placeholderapi.PlaceholderAPI;
import me.smegasberla.apexStaff.commands.ApexCommand;
import me.smegasberla.apexStaff.engine.prediction.PredictionEngine;
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

import java.io.File;
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
  private StaffChatManager staffChatManager = new StaffChatManager(plugin);
  private PredictionEngine predictionEngine = new PredictionEngine();
  private PlaceholderAPIHook papiHook;
  private StatusManager statusManager = new StatusManager(databaseManager, this, papiHook);

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

    setupFolders();

    this.papiHook = new PlaceholderAPIHook(this);
    this.papiHook.registerHook();

    this.sharedManager = new XRayCheckManager(this);
    this.databaseManager = new DatabaseManager(this);
    this.flyManager = new FlyManager();
    this.dupeIPManager = new DupeIPManager();
    this.shadowCamManager = new ShadowCamManager();
    this.freezeManager = new FreezeManager();
    this.vanishManager = new VanishManager();
    this.staffChatManager = new StaffChatManager(plugin);
    this.placeholderManager = new PlaceholderManager(this, vanishManager); 
    this.predictionEngine = new PredictionEngine();
    this.statusManager = new StatusManager(databaseManager, this, papiHook);

    getConfig().options().copyDefaults(true);
    saveDefaultConfig();

    PacketEvents.getAPI().init();

    getCommand("apexstaff").setExecutor(new ApexCommand(sharedManager, flyManager, databaseManager, shadowCamManager, staffChatManager, statusManager));

    getServer().getPluginManager().registerEvents(new MovementListener(this, predictionEngine, statusManager), this);
    getServer().getPluginManager().registerEvents(new DamageListener(this, predictionEngine), this);
    getServer().getPluginManager().registerEvents(new ChatListener(this), this);
    getServer().getPluginManager().registerEvents(new QuitListener(this, databaseManager, sharedManager,dupeIPManager,staffChatManager),this);
    getServer().getPluginManager().registerEvents(new AsyncPreLoginListener(this, databaseManager, sharedManager),
        this);
    getServer().getPluginManager().registerEvents(new BlockBreakListener(this, databaseManager,sharedManager), this);
    getServer().getPluginManager().registerEvents(new JoinListener(this,this, dupeIPManager, databaseManager), this);
    getServer().getPluginManager().registerEvents(new ChatMessageListener(staffChatManager), this);
    getServer().getPluginManager().registerEvents(new MobListener(vanishManager), this);

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
   sender.sendMessage("");
   sender.sendMessage(ChatColor.DARK_GRAY + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
   sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "                    ApexStaff " + ChatColor.DARK_GRAY + "» " + ChatColor.GRAY + "Help Menu");
   sender.sendMessage(ChatColor.DARK_GRAY + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
   sender.sendMessage("");
   sender.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + " ▸ " + ChatColor.YELLOW + "Moderation Commands");
   sender.sendMessage(ChatColor.DARK_AQUA + "   • " + ChatColor.AQUA + "/apexstaff freeze <player>" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "Freeze a player in place");
   sender.sendMessage(ChatColor.DARK_AQUA + "   • " + ChatColor.AQUA + "/apexstaff vanish [player]" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "Toggle vanish mode");
   sender.sendMessage(ChatColor.DARK_AQUA + "   • " + ChatColor.AQUA + "/apexstaff clearchat" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "Clear server chat");
   sender.sendMessage(ChatColor.DARK_AQUA + "   • " + ChatColor.AQUA + "/apexstaff staffchat [message]" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "Toggle or send staff chat");
   sender.sendMessage("");
   sender.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + " ▸ " + ChatColor.YELLOW + "Utility Commands");
   sender.sendMessage(ChatColor.DARK_AQUA + "   • " + ChatColor.AQUA + "/apexstaff fly [player]" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "Toggle flight mode");
   sender.sendMessage(ChatColor.DARK_AQUA + "   • " + ChatColor.AQUA + "/apexstaff ping [player]" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "Check player ping");
   sender.sendMessage(ChatColor.DARK_AQUA + "   • " + ChatColor.AQUA + "/apexstaff shadowcam <player>" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "Spectate player invisibly");
   sender.sendMessage(ChatColor.DARK_AQUA + "   • " + ChatColor.AQUA + "/apexstaff notes <player> <add|list|remove|clear>" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "Manage player notes");
   sender.sendMessage(ChatColor.DARK_AQUA + "   • " + ChatColor.AQUA + "/apexstaff status [player]" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "View player status info");
   sender.sendMessage("");
   sender.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + " ▸ " + ChatColor.YELLOW + "Detection & Anti-Cheat");
   sender.sendMessage(ChatColor.DARK_AQUA + "   • " + ChatColor.AQUA + "/apexstaff xray <player> [info|clear]" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "X-Ray detection checks");
   sender.sendMessage(ChatColor.DARK_AQUA + "   • " + ChatColor.AQUA + "/apexstaff dupeip <player>" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "Check duplicate IP addresses");
   sender.sendMessage("");
   sender.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + " ▸ " + ChatColor.YELLOW + "System & Extensions");
   sender.sendMessage(ChatColor.DARK_AQUA + "   • " + ChatColor.AQUA + "/apexstaff reload" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "Reload plugin configuration");
   sender.sendMessage(ChatColor.DARK_AQUA + "   • " + ChatColor.AQUA + "/apexstaff exstension <list|load|unload|reload>" + ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "Manage extensions");
   sender.sendMessage("");
   sender.sendMessage(ChatColor.DARK_GRAY + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
   sender.sendMessage("");
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

  public void setupFolders() {
    getPlugin().getLogger().info("DEBUG: setupFolders() has started...");

    File dataFolder = getPlugin().getDataFolder();
    if (dataFolder == null) {
      getPlugin().getLogger().severe("DEBUG: getDataFolder() is NULL!");
      return;
    }

    File extensionFolder = new File(dataFolder, "exstensions");

    if (extensionFolder.exists()) {
      getPlugin().getLogger().info("DEBUG: Folder already exists. No action taken.");
    } else {
      getPlugin().getLogger().info("DEBUG: Folder does not exist. Attempting to create...");
      boolean success = extensionFolder.mkdirs();
      getPlugin().getLogger().info("DEBUG: Creation result: " + success);
    }
  }

  public static String formatPlayer(Player player, String message) {

    String world = player.getWorld().getName();

    String coords = String.format("%.1f X %.1f Y %.1f Z", player.getX(), player.getY(), player.getZ());
    double health = player.getHealth();
    int food = player.getFoodLevel();

    User user = PacketEvents.getAPI().getPlayerManager().getUser(player);
    String version = user.getClientVersion().getReleaseName();


    if (message == null || message.isEmpty())
      return "";

    if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
      message = PlaceholderAPI.setPlaceholders(player, message);
    }

    if (message.contains("{online_staff}")) {
      long count = Bukkit.getOnlinePlayers().stream()
          .filter(p -> p.hasPermission("apexstaff.admin"))
          .count();
      message = message.replace("{online_staff}", String.valueOf(count));
    }

    if (player != null) {
      message = message.replace("{player}", player.getName())
          .replace("{displayname}", player.getDisplayName())
          .replace("{uuid}", player.getUniqueId().toString())
          .replace("{ip}", player.getAddress().getHostString())
          .replace("{ping}", String.valueOf(player.getPing()))
          .replace("{coords}", coords)
          .replace("{player}", player.getName())
          .replace("{world}", world)
          .replace("{health}", String.valueOf(health))
          .replace("{food}", String.valueOf(food))
          .replace("{version}", version);


    }

    message = message.replace("{prefix}", "§8[§bApex§8] ");

    return ChatColor.translateAlternateColorCodes('&', message);
  }

  public static String formatTarget(Player target, String message) {

    String world = target.getWorld().getName();
    String coords = String.format("%.1f X %.1f Y %.1f Z", target.getX(), target.getY(), target.getZ());
    double health = target.getHealth();
    int food = target.getFoodLevel();
    User user = PacketEvents.getAPI().getPlayerManager().getUser(target);
    String version = user.getClientVersion().getReleaseName();

    if (message == null || message.isEmpty())
      return "";

    if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
      message = PlaceholderAPI.setPlaceholders(target, message);
    }

    if (message.contains("{online_staff}")) {
      long count = Bukkit.getOnlinePlayers().stream()
          .filter(p -> p.hasPermission("apexstaff.admin"))
          .count();
      message = message.replace("{online_staff}", String.valueOf(count));
    }

    if (target != null) {
      message = message.replace("{target}", target.getName())
          .replace("{displayname}", target.getDisplayName())
          .replace("{uuid}", target.getUniqueId().toString())
          .replace("{ip}", target.getAddress().getHostString())
          .replace("{ping}", String.valueOf(target.getPing()))
          .replace("{coords}", coords)
          .replace("{player}", target.getName())
          .replace("{world}", world)
          .replace("{health}", String.valueOf(health))
          .replace("{food}", String.valueOf(food))
          .replace("{version}", version);

    }

    message = message.replace("{prefix}", "§8[§bApex§8] ");

    return ChatColor.translateAlternateColorCodes('&', message);


  } 


}
