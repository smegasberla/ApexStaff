package me.smegasberla.apexStaff.models;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FreezeModel {

    private static final Map<UUID, FreezeModel> activeBans = new HashMap<>();

    private final int id;
    private final String playerUUID;
    private final String bannedBy;
    private final String reason;
    private final long timeIssued;
    private final long expires;

    public FreezeModel(int id, String playerUUID, String bannedBy, String reason, long timeIssued, long expires) {
        this.id = id;
        this.playerUUID = playerUUID;
        this.bannedBy = bannedBy;
        this.reason = reason;
        this.timeIssued = timeIssued;
        this.expires = expires;
    }

    public static void addBan(UUID uuid, FreezeModel model) {
        activeBans.put(uuid, model);
    }

    public static FreezeModel getBan(UUID uuid) {
        return activeBans.get(uuid);
    }

    public static boolean isBanned(UUID uuid) {
        return activeBans.containsKey(uuid);
    }

    public int getId() { return id; }
    public String getPlayerUUID() { return playerUUID; }
    public String getBannedBy() { return bannedBy; }
    public String getReason() { return reason; }
    public long getTimeIssued() { return timeIssued; }
    public long getExpires() { return expires; }

    public boolean isPermanent() {
        return expires == -1;
    }

    public boolean isExpired() {
        if (isPermanent()) return false;
        return System.currentTimeMillis() > expires;
    }

    public org.bukkit.OfflinePlayer getOfflinePlayer() {
        return org.bukkit.Bukkit.getOfflinePlayer(UUID.fromString(playerUUID));
    }

    public org.bukkit.entity.Player getOnlinePlayer() {
        return org.bukkit.Bukkit.getPlayer(UUID.fromString(playerUUID));
    }

    public static java.util.Set<java.util.UUID> getAllBannedUUIDs() {
        return activeBans.keySet();
    }

    public static void removeBan(UUID uuid) {
        activeBans.remove(uuid);
    }

    public static void clearBans() {
        activeBans.clear();
    }

}