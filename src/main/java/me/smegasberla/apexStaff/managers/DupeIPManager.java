package me.smegasberla.apexStaff.managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class DupeIPManager {

    public HashMap<UUID, String> playerIP = new HashMap<>();
    public HashMap<UUID, Long> lastSeen = new HashMap<>();


    public void addIP(UUID uuid, String IP) {

        playerIP.putIfAbsent(uuid, IP);

    }

    public void removeIP(UUID uuid) {

        playerIP.remove(uuid);

    }

    public List<String> getAllOnlineAlts(UUID uuid) {
        List<String> altsNames = new ArrayList<>();
        Player p = Bukkit.getPlayer(uuid);

        if (p == null) {
            return altsNames;
        }

        String ip = p.getAddress().getHostString();

        for (Player online : Bukkit.getOnlinePlayers()) {

            if (!online.getUniqueId().equals(uuid) &&
                    online.getAddress().getHostString().equals(ip)) {

                altsNames.add(online.getName());
            }
        }

        return altsNames;
    }



}
