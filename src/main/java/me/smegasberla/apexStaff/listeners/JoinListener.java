package me.smegasberla.apexStaff.listeners;

import me.smegasberla.apexStaff.ApexStaff;
import me.smegasberla.apexStaff.managers.DatabaseManager;
import me.smegasberla.apexStaff.managers.DupeIPManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;


public class JoinListener implements Listener {

    private final ApexStaff pluign;
    private final DupeIPManager manager;
    private final DatabaseManager databaseManager;

    public JoinListener(ApexStaff pluign, DupeIPManager manager, DatabaseManager databaseManager) {
        this.pluign = pluign;
        this.manager = manager;
        this.databaseManager = databaseManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {

        UUID uuid = e.getPlayer().getUniqueId();

        String currentIP = e.getPlayer().getAddress().getHostString();

        manager.playerIP.put(uuid, currentIP);

        databaseManager.upsertPlayer(uuid, currentIP);


    }

}
