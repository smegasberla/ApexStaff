package me.smegasberla.apexStaff.listeners;

import me.smegasberla.apexStaff.ApexStaff;
import me.smegasberla.apexStaff.commands.FreezeCommand;
import me.smegasberla.apexStaff.managers.DatabaseManager;
import me.smegasberla.apexStaff.managers.DupeIPManager;
import me.smegasberla.apexStaff.managers.FreezeManager;
import me.smegasberla.apexStaff.managers.XRayCheckManager;
import me.smegasberla.apexStaff.models.FreezeModel;
import me.smegasberla.apexStaff.utils.MessageUtils;
import me.smegasberla.apexStaff.utils.TimeUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class QuitListener implements Listener {

    private final ApexStaff plugin;
    private final DatabaseManager databaseManager;
    private final XRayCheckManager xRayCheckManager;
    private final DupeIPManager dupeIPManager;

    public QuitListener(ApexStaff plugin, DatabaseManager databaseManager, XRayCheckManager xRayCheckManager, DupeIPManager dupeIPManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        this.xRayCheckManager = xRayCheckManager;
        this.dupeIPManager = dupeIPManager;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();

        long now = System.currentTimeMillis();

        dupeIPManager.lastSeen.put(uuid, now);
        dupeIPManager.playerIP.remove(uuid);

        databaseManager.updateLastSeen(uuid, now);

        int totalBlocks = xRayCheckManager.totalBlocks.getOrDefault(uuid, 0);
        int totalOres = xRayCheckManager.totalOres.getOrDefault(uuid, 0);

        DatabaseManager.addXrayData(p,totalBlocks, totalOres);

        if (FreezeManager.isFreezed(p)) {
            if (plugin.getConfig().getBoolean("freeze.ban-on-quit.enabled")) {

                String reason = MessageUtils.getMessage(plugin, "freeze.ban-on-quit.reason");
                String bannedBy = (FreezeCommand.commandExecutor != null) ? FreezeCommand.commandExecutor : "Console";

                long expiry = 0;
                if (plugin.getConfig().getBoolean("freeze.ban-on-quit.temp-ban.enabled")) {
                    String configDuration = plugin.getConfig().getString("freeze.ban-on-quit.temp-ban.time");
                    long rawDuration = TimeUtils.parseTimeToMillis(configDuration);
                    expiry = rawDuration + now;

                    DatabaseManager.addFreezeBan(p, reason, now, expiry, bannedBy);

                    FreezeModel.addBan(uuid, new FreezeModel(0, uuid.toString(), bannedBy, reason, now, expiry));

                } else {
                    DatabaseManager.addFreezeBan(p, reason, now, -1, bannedBy);
                    // ADD THIS: Cache the ban in memory
                    FreezeModel banEntry = new FreezeModel(0, uuid.toString(), bannedBy, reason, now, -1);
                    FreezeModel.addBan(uuid, banEntry);
                }
            }
        }
    }

}
