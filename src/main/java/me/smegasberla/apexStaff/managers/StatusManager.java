package me.smegasberla.apexStaff.managers;

import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

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

    public void statusActionBar(Player p, Player target) {

        if (target == null) return;

        User user = PacketEvents.getAPI().getPlayerManager().getUser(target);
        String targetName = target.getName();

        double health = target.getHealth();
        int food = target.getFoodLevel();
        int ping = target.getPing();
        String version = user.getClientVersion().getReleaseName();
        String world = target.getWorld().getName();
        String coords = String.format("%.1f X %.1f Y %.1f Z", target.getX(), target.getY(), target.getZ());

        String message = MessageUtils.getMessage(plugin, "overlay.message",
                "{target}", targetName,
                "{health}", String.valueOf(health),
                "{food}", String.valueOf(food),
                "{ping}", String.valueOf(ping),
                "{version}", version,
                "{world}", world,
                "{coords}", coords
        );
        if (message == null) return;

        if(p.hasPermission("apexstaff.overlay")) {

            boolean isEnabled = plugin.getConfig().getBoolean("overlay.enabled");

            if(isEnabled){

                p.sendActionBar(message);

            }

        }

    }

    public Player getTargetPlayer(Player staff, double range) {
        RayTraceResult result = staff.getWorld().rayTraceEntities(
            staff.getEyeLocation(), 
            staff.getLocation().getDirection(), 
            range, 
            entity -> (entity instanceof Player && !entity.equals(staff))
        );

        if (result != null && result.getHitEntity() instanceof Player) {
            return (Player) result.getHitEntity();
        }
        return null;
    }

    public boolean isLookingAt(Player observer, Player target, double accuracy) {
    
        Vector toTarget = target.getEyeLocation().toVector().subtract(observer.getEyeLocation().toVector());
        Vector direction = observer.getEyeLocation().getDirection();
        double dot = toTarget.normalize().dot(direction);
    
        return dot > accuracy;
    }

    
}
