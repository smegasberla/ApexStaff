package me.smegasberla.apexStaff.managers;

import me.smegasberla.apexStaff.ApexStaff;
import me.smegasberla.apexStaff.models.FreezeModel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.sqlite.SQLiteDataSource;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class DatabaseManager {

    private final ApexStaff plugin;

    private static SQLiteDataSource dataSource;
    private static Connection connection;

    public DatabaseManager(ApexStaff plugin) {
        this.plugin = plugin;
    }

    public static void init() {
        try {
            File dataFolder = ApexStaff.getPlugin().getDataFolder();
            if (!dataFolder.exists()) {
                dataFolder.mkdirs();
            }

            String dbPath = "jdbc:sqlite:" + new File(dataFolder, "apexstaff.db").getAbsolutePath();

            dataSource = new SQLiteDataSource();
            dataSource.setUrl(dbPath);

            connection = dataSource.getConnection();
            ApexStaff.getPlugin().getLogger().info("SQLite database connection established!");

        } catch (SQLException e) {
            ApexStaff.getPlugin().getLogger().log(Level.SEVERE, "Failed to initialize SQLite database!", e);
        }
    }

    public static void createTables() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("""
            CREATE TABLE IF NOT EXISTS froze_banned(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                uuid TEXT UNIQUE,
                banned_by TEXT DEFAULT "Console",
                reason TEXT DEFAULT "Froze Banned",
                time_issued BIGINT DEFAULT 0,
                expires BIGINT DEFAULT -1
            )
            """);

            stmt.execute("""
            CREATE TABLE IF NOT EXISTS xray_data(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                uuid TEXT UNIQUE,
                total_blocks INTEGER DEFAULT 0,
                total_ores INTEGER DEFAULT 0
            )
            """);

            stmt.execute("""
            CREATE TABLE IF NOT EXISTS dupeip_data (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                uuid TEXT UNIQUE,
                ip TEXT,
                last_seen BIGINT DEFAULT (CAST((julianday('now') - 2440587.5) * 86400000 AS BIGINT))
            );
            CREATE INDEX IF NOT EXISTS idx_ip ON dupeip_data(ip);
            """);

            stmt.execute("""
            CREATE TABLE IF NOT EXISTS apexstaff_notes (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                target_uuid VARCHAR(36) NOT NULL,
                staff_uuid VARCHAR(36) NOT NULL,
                staff_name VARCHAR(16) NOT NULL,
                note_text TEXT NOT NULL,
                timestamp DATETIME DEFAULT CURRENT_TIMESTAMP
            )
            """);

            ApexStaff.getPlugin().getLogger().info("Database tables created successfully!");

        } catch (SQLException e) {
            ApexStaff.getPlugin().getLogger().log(Level.SEVERE, "Failed to create database tables!", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("Database not initialized!");
        }
        return dataSource.getConnection();
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                ApexStaff.getPlugin().getLogger().info("SQLite database connection closed!");
            }
        } catch (SQLException e) {
            ApexStaff.getPlugin().getLogger().log(Level.SEVERE, "Failed to close database connection!", e);
        }
    }


    public static void addFreezeBan(Player p, String reason, long timeIssued, long expires, String bannedBy) {
        String sql = "INSERT OR REPLACE INTO froze_banned (uuid, reason, time_issued, expires, banned_by) VALUES(?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            String uuid = p.getUniqueId().toString();

            pstmt.setString(1, uuid);
            pstmt.setString(2, reason);
            pstmt.setLong(3, timeIssued);
            pstmt.setLong(4, expires);
            pstmt.setString(5, bannedBy);

            pstmt.executeUpdate();
            ApexStaff.getPlugin().getLogger().info("Freeze ban added for player: " + p.getName());

        } catch (SQLException e) {
            ApexStaff.getPlugin().getLogger().log(Level.SEVERE, "Failed to add freeze ban!", e);
        }
    }


    public static boolean isFrozen(String playerUUID) {
        String sql = "SELECT expires FROM froze_banned WHERE uuid = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, playerUUID);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    long expires = rs.getLong("expires");
                    long currentTime = System.currentTimeMillis();

                    return expires == -1 || expires > currentTime;
                }
            }
        } catch (SQLException e) {
            ApexStaff.getPlugin().getLogger().log(Level.SEVERE, "Failed to check freeze status!", e);
        }

        return false;
    }


    public static boolean isFrozen(Player p) {
        return isFrozen(p.getUniqueId().toString());
    }


    public static boolean removeFreezeBan(String playerUUID) {
        String sql = "DELETE FROM froze_banned WHERE uuid = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, playerUUID);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                ApexStaff.getPlugin().getLogger().info("Freeze ban removed for UUID: " + playerUUID);
                return true;
            }
        } catch (SQLException e) {
            ApexStaff.getPlugin().getLogger().log(Level.SEVERE, "Failed to remove freeze ban!", e);
        }

        return false;
    }


    public static boolean removeFreezeBan(Player p) {
        return removeFreezeBan(p.getUniqueId().toString());
    }


    public static FreezeModel getFreezeBan(String playerUUID) {
        String sql = "SELECT * FROM froze_banned WHERE uuid = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, playerUUID);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new FreezeModel(
                            rs.getInt("id"),
                            rs.getString("uuid"),
                            rs.getString("banned_by"),
                            rs.getString("reason"),
                            rs.getLong("time_issued"),
                            rs.getLong("expires")
                    );
                }
            }
        } catch (SQLException e) {
            ApexStaff.getPlugin().getLogger().log(Level.SEVERE, "Failed to get freeze ban info!", e);
        }

        return null;
    }


    public static List<FreezeModel> getAllActiveFreezeBans() {
        List<FreezeModel> freezeBans = new ArrayList<>();
        String sql = "SELECT * FROM froze_banned";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            long currentTime = System.currentTimeMillis();

            while (rs.next()) {
                long expires = rs.getLong("expires");

                if (expires == -1 || expires > currentTime) {
                    freezeBans.add(new FreezeModel(
                            rs.getInt("id"),
                            rs.getString("uuid"),
                            rs.getString("banned_by"),
                            rs.getString("reason"),
                            rs.getLong("time_issued"),
                            expires
                    ));
                }
            }
        } catch (SQLException e) {
            ApexStaff.getPlugin().getLogger().log(Level.SEVERE, "Failed to get all freeze bans!", e);
        }

        return freezeBans;
    }

    public static int cleanupExpiredBans() {
        String sql = "DELETE FROM froze_banned WHERE expires != -1 AND expires < ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, System.currentTimeMillis());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                ApexStaff.getPlugin().getLogger().info("Cleaned up " + rowsAffected + " expired freeze bans");
            }
            return rowsAffected;
        } catch (SQLException e) {
            ApexStaff.getPlugin().getLogger().log(Level.SEVERE, "Failed to cleanup expired bans!", e);
        }

        return 0;
    }

    public static void addXrayData(Player p, int totalBlock, int totalOres) {

        String sql = "INSERT INTO xray_data (uuid, total_blocks, total_ores) VALUES (?, ?, ?) " +
                "ON CONFLICT(uuid) DO UPDATE SET " +
                "total_blocks = excluded.total_blocks, " +
                "total_ores = excluded.total_ores";

        try(PreparedStatement ppstm = connection.prepareStatement(sql)) {

            UUID uuid = p.getUniqueId();

            ppstm.setString(1, uuid.toString());
            ppstm.setInt(2, totalBlock);
            ppstm.setInt(3, totalOres);
            ppstm.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void removeXrayData(Player p) {
        String sql = "UPDATE xray_data SET total_blocks = 0, total_ores = 0 WHERE uuid = ?";

        try (PreparedStatement ppstm = connection.prepareStatement(sql)) {
            UUID uuid = p.getUniqueId();

            ppstm.setString(1, uuid.toString());
            ppstm.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getXrayData(Player p, XRayCheckManager manager) {
        String sql = "SELECT total_blocks, total_ores FROM xray_data WHERE uuid = ?";

        try (PreparedStatement ppstm = connection.prepareStatement(sql)) {
            ppstm.setString(1, p.getUniqueId().toString());


            try (ResultSet rs = ppstm.executeQuery()) {
                if (rs.next()) {

                    int blocks = rs.getInt("total_blocks");
                    int ores = rs.getInt("total_ores");


                    manager.totalBlocks.put(p.getUniqueId(), blocks);
                    manager.totalOres.put(p.getUniqueId(), ores);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getXrayData(UUID uuid, XRayCheckManager manager) {
        String sql = "SELECT total_blocks, total_ores FROM xray_data WHERE uuid = ?";

        try (PreparedStatement ppstm = connection.prepareStatement(sql)) {

            ppstm.setString(1, uuid.toString());

            try (ResultSet rs = ppstm.executeQuery()) {
                if (rs.next()) {
                    int blocks = rs.getInt("total_blocks");
                    int ores = rs.getInt("total_ores");

                    
                    manager.totalBlocks.put(uuid, blocks);
                    manager.totalOres.put(uuid, ores);
                }
            }
        } catch (SQLException e) {
            System.err.println("[ApexStaff] Error loading X-Ray data for UUID: " + uuid);
            e.printStackTrace();
        }
    }

    public void upsertPlayer(UUID uuid, String ip) {

        String sql = "INSERT INTO dupeip_data (uuid, ip, last_seen) VALUES (?, ?, ?) " +
                "ON CONFLICT(uuid) DO UPDATE SET ip = excluded.ip, last_seen = excluded.last_seen;";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, uuid.toString());
            stmt.setString(2, ip);
            stmt.setLong(3, System.currentTimeMillis());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<UUID> getPlayersByIP(String ip) {
        List<UUID> players = new ArrayList<>();
        String sql = "SELECT uuid FROM dupeip_data WHERE ip = ?;";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, ip);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                players.add(UUID.fromString(rs.getString("uuid")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return players;
    }

    public void removePlayer(UUID uuid) {
        String sql = "DELETE FROM dupeip_data WHERE uuid = ?;";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, uuid.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void purgeOldData(long days) {
        long cutoff = System.currentTimeMillis() - (days * 24 * 60 * 60 * 1000L);
        String sql = "DELETE FROM dupeip_data WHERE last_seen < ?;";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, cutoff);
            int deleted = stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getAlts(UUID targetUuid) {
        String sql = "SELECT uuid FROM dupeip_data WHERE ip = (SELECT ip FROM dupeip_data WHERE uuid = ?) AND uuid != ?;";

        List<String> altNames = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, targetUuid.toString());
            stmt.setString(2, targetUuid.toString());

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String uuidStr = rs.getString("uuid");
                UUID altUuid = UUID.fromString(uuidStr);


                Player onlinePlayer = Bukkit.getPlayer(altUuid);

                if (onlinePlayer != null) {
                    altNames.add(onlinePlayer.getName());
                } else {

                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(altUuid);
                    String name = offlinePlayer.getName();

                    if (name != null) {
                        altNames.add(name);
                    } else {

                        altNames.add("Unknown (" + uuidStr + ")");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return altNames;
    }

    public void updateLastSeen(UUID uuid, long lastSeen) {
        String sql = "INSERT INTO dupeip_data (uuid, last_seen) VALUES (?, ?) " +
                "ON CONFLICT(uuid) DO UPDATE SET last_seen = excluded.last_seen;";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)){

            pstmt.setString(1, uuid.toString());
            pstmt.setLong(2, lastSeen);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getAltCount(UUID targetUUID) {

        String sql = "SELECT COUNT(*) FROM dupeip_data WHERE ip = " +
                "(SELECT ip FROM dupeip_data WHERE uuid = ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, targetUUID.toString());
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int totalOnIP = rs.getInt(1);

                return Math.max(0, totalOnIP - 1);
            }
        } catch (SQLException e) {
            System.out.println("Error calculating alts: " + e.getMessage());
        }
        return 0;
    }

    public long getLastSeen(UUID uuid) {

        String sql = "SELECT last_seen FROM dupeip_data WHERE uuid = ?;";

        try (PreparedStatement ppstm = connection.prepareStatement(sql)) {
            ppstm.setString(1, uuid.toString());


            try (ResultSet rs = ppstm.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("last_seen");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0L;
    }

    public long getLastSeenByName(String name) {
        org.bukkit.OfflinePlayer op = org.bukkit.Bukkit.getOfflinePlayer(name);
        return getLastSeen(op.getUniqueId());
    }

    public void addNote(UUID target, UUID staff, String staffName, String text) {
        String sql = "INSERT INTO apexstaff_notes (target_uuid, staff_uuid, staff_name, note_text) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, target.toString());
            pstmt.setString(2, staff.toString());
            pstmt.setString(3, staffName);
            pstmt.setString(4, text);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getNotes(UUID target, String targetName) {
        List<String> notesList = new ArrayList<>();


        String template = plugin.getConfig().getString("note-template", "[#{id}] {target}: {note}");

        String sql = "SELECT id, note_text, staff_name, timestamp FROM apexstaff_notes WHERE target_uuid = ? ORDER BY id DESC";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, target.toString());

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {

                    String id = String.valueOf(rs.getInt("id"));
                    String note = rs.getString("note_text");
                    String staff = rs.getString("staff_name");
                    String time = rs.getString("timestamp");


                    String formattedNote = template
                            .replace("{id}", id)
                            .replace("{target}", targetName)
                            .replace("{note}", note)
                            .replace("{player}", staff)
                            .replace("{time}", time);


                    notesList.add(ChatColor.translateAlternateColorCodes('&', formattedNote));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return notesList;
    }

    public boolean removeNote(int noteId, UUID target) {
        String sql = "DELETE FROM apexstaff_notes WHERE id = ? AND target_uuid = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, noteId);
            pstmt.setString(2, target.toString());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void clearPlayerNotes(UUID target) {
        String sql = "DELETE FROM apexstaff_notes WHERE target_uuid = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, target.toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getNoteCount(UUID target) {
        String sql = "SELECT COUNT(*) FROM apexstaff_notes WHERE target_uuid = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, target.toString());

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getLastNoteId() {

        String sql = "SELECT last_insert_rowid()";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public String getStaffUUID(UUID targetUUID) {

        String sql = "SELECT staff_uuid FROM apexstaff_notes WHERE target_uuid = ? ORDER BY id DESC";

        try(PreparedStatement ppstm = connection.prepareStatement(sql)) {

            ppstm.setString(1,  targetUUID.toString());

            try (ResultSet rs = ppstm.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("staff_uuid");
                }
            }

        }catch(SQLException e){

            e.printStackTrace();

        }

        return null;

    }





}
