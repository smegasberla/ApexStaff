package me.smegasberla.apexStaff.managers;

import me.smegasberla.apexStaff.ApexStaff;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class XRayCheckManager {

    private XRayCheckManager manager;

    private final ApexStaff plugin;

    public HashMap<UUID, Integer> totalBlocks = new HashMap<>();
    public HashMap<UUID, Integer> totalOres = new HashMap<>();

    public XRayCheckManager(ApexStaff plugin) {
        this.plugin = plugin;
    }

    public double calcutaleXRayPercentage(Player p) {

        FileConfiguration config = plugin.getConfig();

        UUID uuid = p.getUniqueId();

        int totalBlockNumber = totalBlocks.getOrDefault(uuid, 0);
        int totalOreNumber = totalOres.getOrDefault(uuid, 0);

        if (totalBlockNumber == 0) {
            return 0.0;
        }

        double percentage = ((double) totalOreNumber / totalBlockNumber) * 100;

        return percentage;
    }

    public XRayCheckManager getManager() {
        return manager;
    }

    public List<String> getMaterialList() {

        List<String> materialListCheck = plugin.getConfig().getStringList("xray.xray-blocks");

        return materialListCheck;

    }

    public boolean isSuspect(Player p) {
        UUID uuid = p.getUniqueId();
        int totalMined = totalBlocks.getOrDefault(uuid, 0);
        int minBlocks = plugin.getConfig().getInt("xray.amount-of-blocks");
        double percentage = calcutaleXRayPercentage(p);
        double threshold = plugin.getConfig().getDouble("xray.alert-threshold");

        return totalMined >= minBlocks && percentage >= threshold;
    }
}
