package me.smegasberla.testexstension.commands.handlers;

import me.smegasberla.testexstension.TestExstension;
import org.bukkit.entity.Player;

public class UpdateCommand {

    private final TestExstension extension;

    public UpdateCommand(TestExstension extension) {
        this.extension = extension;
    }

    public void execute(Player player) {
        extension.getMessageManager().sendMessage(player, "&eUpdate check test:");
        extension.getMessageManager().sendMessage(player, "&7Current Version: &e" + extension.getVersion());
    }
}
