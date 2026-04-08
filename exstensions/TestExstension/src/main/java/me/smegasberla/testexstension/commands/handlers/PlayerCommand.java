package me.smegasberla.testexstension.commands.handlers;

import me.smegasberla.testexstension.TestExstension;
import org.bukkit.entity.Player;

public class PlayerCommand {

    private final TestExstension extension;

    public PlayerCommand(TestExstension extension) {
        this.extension = extension;
    }

    public void execute(Player player) {
        extension.getMessageManager().sendMessage(player, "&ePlayer Utils Test:");
        extension.getMessageManager().sendMessage(player, "&7Online Count: &e" + extension.getPlayerManager().getOnlinePlayerCount());
        extension.getMessageManager().sendMessage(player, "&7Has Players: &e" + extension.getPlayerManager().hasPlayers());
        extension.getPlayerManager().forEachOnlinePlayer(p -> {
            extension.getMessageManager().sendMessage(player, "&7- " + p.getName());
        });
    }
}
