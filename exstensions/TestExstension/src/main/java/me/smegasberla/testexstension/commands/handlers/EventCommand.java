package me.smegasberla.testexstension.commands.handlers;

import me.smegasberla.testexstension.TestExstension;
import org.bukkit.entity.Player;

public class EventCommand {

    private final TestExstension extension;

    public EventCommand(TestExstension extension) {
        this.extension = extension;
    }

    public void execute(Player player) {
        extension.getMessageManager().sendMessage(player, "&eEvent registration test complete!");
        extension.getMessageManager().sendMessage(player, "&7Events are registered via getEventManager()");
    }
}
