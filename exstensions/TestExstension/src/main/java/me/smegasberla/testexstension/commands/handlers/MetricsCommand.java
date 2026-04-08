package me.smegasberla.testexstension.commands.handlers;

import me.smegasberla.testexstension.TestExstension;
import org.bukkit.entity.Player;

public class MetricsCommand {

    private final TestExstension extension;

    public MetricsCommand(TestExstension extension) {
        this.extension = extension;
    }

    public void execute(Player player) {
        extension.getMessageManager().sendMessage(player, "&eMetrics test - checking server stats:");
        extension.getMessageManager().sendMessage(player, "&7Player Count: &e" + extension.getMetricsManager("test").getPlayerCount());
        extension.getMessageManager().sendMessage(player, "&7Max Players: &e" + extension.getMetricsManager("test").getMaxPlayers());
        extension.getMessageManager().sendMessage(player, "&7Server Version: &e" + extension.getMetricsManager("test").getServerVersion());
    }
}
