package me.smegasberla.apexStaff.managers;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;


public class FlyManager {

    public HashSet<UUID> customFlight = new HashSet<>();

    public void setFly(Player p) {

        customFlight.add(p.getUniqueId());
        p.setAllowFlight(true);

    }

    public void removeFly(Player p) {

        customFlight.remove(p.getUniqueId());
        p.setAllowFlight(false);

    }

    public boolean isFlying(Player p) {

        if(p.isFlying()) {

            return true;

        }

        return false;

    }

    public boolean getAllowedFlight(Player p) {


        return p.getAllowFlight();

    }

    public void toggleFly(Player  p) {

        if(customFlight.contains(p.getUniqueId())) {
            if(p.getGameMode() == GameMode.CREATIVE) {
                customFlight.remove(p.getUniqueId());
                p.setAllowFlight(true);
                return;

            }
            removeFly(p);



        } else {

            setFly(p);

        }

    }


}
