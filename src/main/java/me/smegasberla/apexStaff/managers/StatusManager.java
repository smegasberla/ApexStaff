package me.smegasberla.apexStaff.managers;

import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.player.User;

import me.smegasberla.apexStaff.ApexStaff;
import me.smegasberla.apexStaff.utils.MessageUtils;
import me.smegasberla.apexStaff.utils.TimeUtils;

public class StatusManager {

    private final DatabaseManager databaseManager;
    private final ApexStaff plugin;

    public StatusManager(DatabaseManager databaseManager, ApexStaff plugin) {

        this.databaseManager = databaseManager;
        this.plugin = plugin;

    }

    public void statusMethod(Player p, Player target) {
        if (target == null) return;

        User user = PacketEvents.getAPI().getPlayerManager().getUser(target);
        String targetName = target.getName();

        double health = target.getHealth();
        int food = target.getFoodLevel();
        int ping = target.getPing();
        String version = user.getClientVersion().getReleaseName();
        String world = target.getWorld().getName();
        String coords = String.format("%.1f X %.1f Y %.1f Z", target.getX(), target.getY(), target.getZ());
        String firstPlayed = TimeUtils.formatTimestamp(target.getFirstPlayed());
        UUID uuid = target.getUniqueId();
        String stringUUID = uuid.toString();
        String IP = MessageUtils.censorIPs(target.getAddress().getHostString());
        String altNumber = String.valueOf(databaseManager.getAltCount(uuid));

        String message = MessageUtils.getMessage(plugin, "status-message",
                "{target}", targetName,
                "{health}", String.valueOf(health),
                "{food}", String.valueOf(food),
                "{ping}", String.valueOf(ping),
                "{version}", version,
                "{world}", world,
                "{coords}", coords,
                "{first_joined}", firstPlayed,
                "{uuid}", stringUUID,
                "{ip}", IP,
                "{alt_number}", altNumber
        );
        if (message == null) return;
        p.sendMessage(message);
    }

    
}
