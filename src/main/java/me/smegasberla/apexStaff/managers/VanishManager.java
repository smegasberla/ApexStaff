package me.smegasberla.apexStaff.managers;

import me.smegasberla.apexStaff.ApexStaff;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.UUID;

public class VanishManager {


    public static HashSet<UUID> vanishedPlayers = new HashSet<>();

    public static void vanishPlayer(Player p) {
        UUID uuid = p.getUniqueId();
        ApexStaff plugin = ApexStaff.getPlugin(ApexStaff.class);

        vanishedPlayers.add(uuid);


        for (Player online : Bukkit.getOnlinePlayers()) {
            if (!online.equals(p)) {
                online.hidePlayer(plugin, p);
            }
        }
    }

    public static void unvanishPlayer(Player p) {
        UUID uuid = p.getUniqueId();
        ApexStaff plugin = ApexStaff.getPlugin(ApexStaff.class);

        vanishedPlayers.remove(uuid);


        for (Player online : Bukkit.getOnlinePlayers()) {
            online.showPlayer(plugin, p);
        }
    }

    public static boolean isVanished(Player p) {

        UUID uuid = p.getUniqueId();
        ApexStaff plugin = ApexStaff.getPlugin(ApexStaff.class);

        if(vanishedPlayers.contains(uuid)) {

            return true;

        } else {

            return false;

        }

    }


}
