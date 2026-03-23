package me.smegasberla.apexStaff.listeners;

import me.smegasberla.apexStaff.ApexStaff;
import me.smegasberla.apexStaff.managers.DatabaseManager;
import me.smegasberla.apexStaff.managers.FreezeManager;
import me.smegasberla.apexStaff.managers.XRayCheckManager;
import me.smegasberla.apexStaff.models.FreezeModel;
import me.smegasberla.apexStaff.utils.MessageUtils;
import me.smegasberla.apexStaff.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;

import java.net.http.WebSocket;
import java.util.UUID;

import static org.bukkit.event.player.AsyncPlayerPreLoginEvent.*;

public class JoinListener implements Listener {

    private final ApexStaff plugin;
    private final DatabaseManager databaseManager;
    private final XRayCheckManager xrayManager;

    public JoinListener(ApexStaff plugin, DatabaseManager databaseManager, XRayCheckManager xrayManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        this.xrayManager = xrayManager;
    }

    @EventHandler
    public void onPlayerJoin(AsyncPlayerPreLoginEvent e) {
        java.util.UUID uuid = e.getUniqueId();

        DatabaseManager.getXrayData(uuid, xrayManager);

        if (!FreezeModel.isBanned(uuid)) {
            return;
        }

        FreezeModel model = FreezeModel.getBan(uuid);



        if (model.isPermanent()) {
            e.setResult(PlayerPreLoginEvent.Result.KICK_BANNED);
            String msg = MessageUtils.getMessage(plugin, "quit-banned-permanent", "{player}", model.getBannedBy(), "{reason}", model.getReason());
            if (msg != null) e.setKickMessage(msg);
        }
        else if (!model.isExpired()) {
            e.setResult(PlayerPreLoginEvent.Result.KICK_BANNED);
            long remainingMillis = model.getExpires() - System.currentTimeMillis();
            long remainingSeconds = remainingMillis / 1000;
            String time = TimeUtils.formatDuration(remainingSeconds);
            String msg = MessageUtils.getMessage(plugin, "quit-banned-temporary", "{player}", model.getBannedBy(), "{expiry}", time, "{reason}", model.getReason());
            if (msg != null) e.setKickMessage(msg);
            FreezeManager.freezedPlayers.remove(uuid);

        }
    }

}
