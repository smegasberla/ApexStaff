package me.smegasberla.exstension.api.manager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class NMSManager {

    private final String nmsVersion;
    private final String cbVersion;

    public NMSManager() {
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        nmsVersion = getNMSVersion();
        cbVersion = packageName.substring(packageName.lastIndexOf('.') + 1);
    }

    public String getNMSVersion() {
        try {
            Class<?> craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + cbVersion + ".entity.CraftPlayer");
            Method getHandleMethod = craftPlayerClass.getMethod("getHandle");
            Object entityPlayer = getHandleMethod.invoke(null);
            Field versionField = entityPlayer.getClass().getField("version");
            return versionField.get(null).toString();
        } catch (Exception e) {
            return "unknown";
        }
    }

    public String getCraftBukkitVersion() {
        return cbVersion;
    }

    public Class<?> getNMSClass(String className) throws ClassNotFoundException {
        return Class.forName("net.minecraft.server." + nmsVersion + "." + className);
    }

    public Class<?> getCraftBukkitClass(String className) throws ClassNotFoundException {
        return Class.forName("org.bukkit.craftbukkit." + cbVersion + "." + className);
    }

    public Object getHandle(Object obj) {
        try {
            Method getHandleMethod = obj.getClass().getMethod("getHandle");
            return getHandleMethod.invoke(obj);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isNMSClass(Object obj, String className) {
        return obj.getClass().getName().contains(className);
    }

    public boolean isCraftBukkitClass(Object obj, String className) {
        return obj.getClass().getName().contains("org.bukkit.craftbukkit") && obj.getClass().getName().contains(className);
    }

    public String getServerVersion() {
        return Bukkit.getServer().getVersion();
    }

    public String getBukkitVersion() {
        return Bukkit.getServer().getBukkitVersion();
    }

    public int getProtocolVersion(Player player) {
        try {
            Object craftPlayer = getHandle(player);
            Object entityPlayer = getHandle(craftPlayer);
            if (entityPlayer != null) {
                Field connectionField = entityPlayer.getClass().getField("playerConnection");
                Object connection = connectionField.get(entityPlayer);
                if (connection != null) {
                    Field networkManagerField = connection.getClass().getField("networkManager");
                    Object networkManager = networkManagerField.get(connection);
                    Field versionField = networkManager.getClass().getField("protocolVersion");
                    return (int) versionField.get(networkManager);
                }
            }
        } catch (Exception e) {
            return -1;
        }
        return -1;
    }
}
