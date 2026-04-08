package me.smegasberla.testexstension.commands.handlers;

import me.smegasberla.testexstension.TestExstension;
import org.bukkit.entity.Player;

public class DatabaseCommand {

    private final TestExstension extension;

    public DatabaseCommand(TestExstension extension) {
        this.extension = extension;
    }

    public void execute(Player player) {
        extension.getMessageManager().sendMessage(player, "&eDatabase manager ready!");
        extension.getMessageManager().sendMessage(player, "&7Use getDatabaseManager() to create connections");
    }
}
