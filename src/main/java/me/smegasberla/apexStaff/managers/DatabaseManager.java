package me.smegasberla.apexStaff.managers;

import me.smegasberla.apexStaff.ApexStaff;
import me.smegasberla.apexStaff.models.FreezeModel;
import org.bukkit.entity.Player;
import org.sqlite.SQLiteDataSource;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class DatabaseManager {

    private static SQLiteDataSource dataSource;
    private static Connection connection;

    public static void init() {
        try {
            // Create database file in plugin data folder
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

                    // If expires is -1, it's a permanent ban
                    // If expires is greater than current time, the ban is still active
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

                // Only add active bans (permanent or not expired)
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
        // We use UPDATE to change the values to 0 for a specific UUID
        String sql = "UPDATE xray_data SET total_blocks = 0, total_ores = 0 WHERE uuid = ?";

        try (PreparedStatement ppstm = connection.prepareStatement(sql)) {
            UUID uuid = p.getUniqueId();

            ppstm.setString(1, uuid.toString());
            ppstm.executeUpdate(); // Use executeUpdate for changes

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
            // We use the uuid passed in the parameter instead of p.getUniqueId()
            ppstm.setString(1, uuid.toString());

            try (ResultSet rs = ppstm.executeQuery()) {
                if (rs.next()) {
                    int blocks = rs.getInt("total_blocks");
                    int ores = rs.getInt("total_ores");

                    // Update the manager's HashMaps using the UUID
                    manager.totalBlocks.put(uuid, blocks);
                    manager.totalOres.put(uuid, ores);
                }
            }
        } catch (SQLException e) {
            System.err.println("[ApexStaff] Error loading X-Ray data for UUID: " + uuid);
            e.printStackTrace();
        }
    }
}
