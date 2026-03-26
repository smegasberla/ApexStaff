package me.smegasberla.apexStaff;

import com.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import me.smegasberla.apexStaff.commands.ApexCommand;
import me.smegasberla.apexStaff.listeners.*;
import me.smegasberla.apexStaff.managers.DatabaseManager;
import me.smegasberla.apexStaff.managers.DupeIPManager;
import me.smegasberla.apexStaff.managers.FlyManager;
import me.smegasberla.apexStaff.managers.XRayCheckManager;
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
    private final XRayCheckManager sharedManager = new XRayCheckManager(this);
    private final DatabaseManager databaseManager = new DatabaseManager();
    private final FlyManager flyManager = new FlyManager();
    private final DupeIPManager dupeIPManager = new DupeIPManager();



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

        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        PacketEvents.getAPI().init();

        getCommand("apexstaff").setExecutor(new ApexCommand(sharedManager, flyManager, databaseManager));


        getServer().getPluginManager().registerEvents(new MovementListener(this), this);
        getServer().getPluginManager().registerEvents(new DamageListener(this), this);
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        getServer().getPluginManager().registerEvents(new QuitListener(this, databaseManager, sharedManager, dupeIPManager), this);
        getServer().getPluginManager().registerEvents(new AsyncPreLoginListener(this, databaseManager, sharedManager), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(this, sharedManager), this);
        getServer().getPluginManager().registerEvents(new JoinListener(this, dupeIPManager, databaseManager), this);


        DatabaseManager.init();
        DatabaseManager.createTables();


    }

    @Override
    public void onDisable() {

        DatabaseManager.closeConnection();
        PacketEvents.getAPI().terminate();

    }

    public static void sendHelpMessage(CommandSender sender) {
        sender.sendMessage(ChatColor.DARK_GRAY + "=================================");
        sender.sendMessage(ChatColor.GOLD + "        ApexStaff Help Menu");
        sender.sendMessage(ChatColor.DARK_GRAY + "=================================");
        sender.sendMessage("");
        sender.sendMessage(ChatColor.AQUA + "/apexstaff" + ChatColor.GRAY + " - " + ChatColor.WHITE + "Show this help message");
        sender.sendMessage(ChatColor.AQUA + "/apexstaff reload" + ChatColor.GRAY + " - " + ChatColor.WHITE + "Reload the plugin configuration");
        sender.sendMessage(ChatColor.AQUA + "/apexstaff vanish <player>" + ChatColor.GRAY + " - " + ChatColor.WHITE + "Toggle vanish mode");
        sender.sendMessage(ChatColor.AQUA + "/apexstaff freeze <player>" + ChatColor.GRAY + " - " + ChatColor.WHITE + "Freeze a player");
        sender.sendMessage(ChatColor.AQUA + "/apexstaff xray <player> [info|clear]" + ChatColor.GRAY + " - " + ChatColor.WHITE + "Get info on a player regarding xray checks");
        sender.sendMessage("");
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
