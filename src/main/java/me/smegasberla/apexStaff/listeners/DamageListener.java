package me.smegasberla.apexStaff.listeners;

import me.smegasberla.apexStaff.ApexStaff;
import me.smegasberla.apexStaff.managers.FreezeManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.UUID;

public class DamageListener implements Listener {

    private final ApexStaff plugin;

    public DamageListener(ApexStaff plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {

        Entity damager = e.getDamager();
        Entity victim = e.getEntity();

        boolean damageBoolean = plugin.getConfig().getBoolean("freeze.block-damage");

        if(damageBoolean == true) {

            if(damager instanceof Player attacker) {

                if(FreezeManager.isFreezed(attacker)) {

                    e.setCancelled(true);

                }

            }

            if(victim instanceof Player p) {

                if(FreezeManager.isFreezed(p)) {

                    e.setCancelled(true);

                }

            }

        }

    }


}