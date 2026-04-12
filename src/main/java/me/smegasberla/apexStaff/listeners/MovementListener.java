package me.smegasberla.apexStaff.listeners;

import me.smegasberla.apexStaff.ApexStaff;
import me.smegasberla.apexStaff.managers.FreezeManager;
import me.smegasberla.apexStaff.utils.MessageUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.UUID;

public class MovementListener implements Listener {

    private final ApexStaff plugin;

    public MovementListener(ApexStaff plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {

        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();

        String message = MessageUtils.getMessage(plugin, "movement-disallowed");

        if(FreezeManager.isFreezed(p)) {

            e.setCancelled(true);
            if(message != null) {

                p.sendMessage(message);

            }

        }

    }

}







