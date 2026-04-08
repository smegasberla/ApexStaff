package me.smegasberla.testexstension.commands.handlers;

import me.smegasberla.testexstension.TestExstension;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class LocationCommand {

    private final TestExstension extension;

    public LocationCommand(TestExstension extension) {
        this.extension = extension;
    }

    public void execute(Player player) {
        Location loc = player.getLocation();
        extension.getLocationManager().saveLocation("testspawn", loc);
        extension.getMessageManager().sendMessage(player, "&eSaved location: testspawn");
        extension.getMessageManager().sendMessage(player, "&7X: &e" + loc.getBlockX());
        extension.getMessageManager().sendMessage(player, "&7Y: &e" + loc.getBlockY());
        extension.getMessageManager().sendMessage(player, "&7Z: &e" + loc.getBlockZ());
        extension.getMessageManager().sendMessage(player, "&7World: &e" + loc.getWorld().getName());

        Location saved = extension.getLocationManager().getLocation("testspawn");
        if (saved != null) {
            extension.getMessageManager().sendMessage(player, "&eRetrieved saved location!");
        }
    }
}
