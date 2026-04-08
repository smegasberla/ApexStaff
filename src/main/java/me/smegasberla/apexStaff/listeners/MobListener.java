package me.smegasberla.apexStaff.listeners;

import me.smegasberla.apexStaff.managers.VanishManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

import java.util.UUID;

public class MobListener implements Listener {

    private final VanishManager manager;

    public MobListener(VanishManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onEntityTargetLivingEntityEvent(EntityTargetLivingEntityEvent e) {
        Entity target = e.getTarget();

        if(target instanceof Player p) {

            UUID pUUID = p.getUniqueId();

            if(VanishManager.vanishedPlayers.contains(pUUID)) {

                e.setTarget(null);
                e.setCancelled(true);

            }
        }
    }
}
