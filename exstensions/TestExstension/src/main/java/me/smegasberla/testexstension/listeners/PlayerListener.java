package me.smegasberla.testexstension.listeners;

import me.smegasberla.testexstension.TestExstension;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener {

    private final TestExstension extension;

    public PlayerListener(TestExstension extension) {
        this.extension = extension;
    }

    public void onPlayerJoin(PlayerJoinEvent event) {
        if (event.getPlayer().hasPermission("testexstension.admin")) {
            extension.getMessageManager().sendMessage(event.getPlayer(), "&eTestExstension &7- Player joined: " + event.getPlayer().getName());
        }
    }

    public void onPlayerQuit(PlayerQuitEvent event) {
        if (event.getPlayer().hasPermission("testexstension.admin")) {
            extension.getMessageManager().sendMessage(event.getPlayer(), "&eTestExstension &7- Player quit: " + event.getPlayer().getName());
        }
    }
}
