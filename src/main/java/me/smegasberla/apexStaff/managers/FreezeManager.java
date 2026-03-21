package me.smegasberla.apexStaff.managers;

import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.UUID;

public class FreezeManager {

    public static HashSet<UUID> freezedPlayers = new HashSet<>();


    public static void freezePlayer(Player p) {

        UUID uuid = p.getUniqueId();
        freezedPlayers.add(uuid);


    }

    public static void unfreezePlayer(Player p) {

        UUID uuid = p.getUniqueId();
        freezedPlayers.remove(uuid);


    }

    public static boolean isFreezed(Player p) {

        UUID uuid = p.getUniqueId();
        if(freezedPlayers.contains(uuid)) {

            return true;

        } else {

            return false;

        }

    }



}
