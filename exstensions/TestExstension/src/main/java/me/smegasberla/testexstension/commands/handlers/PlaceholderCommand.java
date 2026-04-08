package me.smegasberla.testexstension.commands.handlers;

import me.smegasberla.testexstension.TestExstension;
import org.bukkit.entity.Player;

public class PlaceholderCommand {

    private final TestExstension extension;

    public PlaceholderCommand(TestExstension extension) {
        this.extension = extension;
    }

    public void execute(Player player) {
        if (extension.getPlaceholderManager().isPlaceholderAPIEnabled()) {
            extension.getPlaceholderManager().registerExpansion("testext", "TestExstension", "1.0.0");
            extension.getPlaceholderManager().registerPlaceholder("test_value", p -> "TestValue");
            extension.getMessageManager().sendMessage(player, "&ePlaceholderAPI enabled! Registered test expansion.");
        } else {
            extension.getMessageManager().sendMessage(player, "&cPlaceholderAPI not enabled!");
        }
    }
}
