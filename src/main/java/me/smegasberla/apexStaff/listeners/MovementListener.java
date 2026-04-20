package me.smegasberla.apexStaff.listeners;

import me.smegasberla.apexStaff.ApexStaff;
import me.smegasberla.apexStaff.engine.prediction.PredictionEngine;
import me.smegasberla.apexStaff.managers.FreezeManager;
import me.smegasberla.apexStaff.managers.StatusManager;
import me.smegasberla.apexStaff.utils.MessageUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.UUID;

public class MovementListener implements Listener {

    private final ApexStaff plugin;
    private final PredictionEngine predictionEngine;
    private final StatusManager manager;

    public MovementListener(ApexStaff plugin, PredictionEngine predictionEngine, StatusManager manager) {
        this.plugin = plugin;
        this.predictionEngine = predictionEngine;
        this.manager = manager;
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

    @EventHandler
    public void onStaffLook(PlayerMoveEvent event) {
        Player staff = event.getPlayer(); 
    
        if (event.getFrom().getYaw() == event.getTo().getYaw() && 
        event.getFrom().getPitch() == event.getTo().getPitch()) return;

        int range = plugin.getConfig().getInt("overlay.range");
        double accuracy = 0.96;
    
        Player target = manager.getTargetPlayer(staff, range);
        if (target != null && (manager.isLookingAt(staff, target, accuracy))) {
        
            manager.statusActionBar(staff, target);


        }
    }

}







