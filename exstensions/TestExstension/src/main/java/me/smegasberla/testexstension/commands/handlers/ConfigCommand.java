package me.smegasberla.testexstension.commands.handlers;

import me.smegasberla.testexstension.TestExstension;
import org.bukkit.entity.Player;

public class ConfigCommand {

    private final TestExstension extension;

    public ConfigCommand(TestExstension extension) {
        this.extension = extension;
    }

    public void execute(Player player) {
        extension.getConfigManager().set("test.string", "Hello from config!");
        extension.getConfigManager().set("test.number", 123);
        extension.getConfigManager().set("test.boolean", true);
        extension.getConfigManager().save();
        extension.getMessageManager().sendMessage(player, "&eConfig test values saved!");
        extension.getMessageManager().sendMessage(player, "&7String: &e" + extension.getConfigManager().getString("test.string"));
        extension.getMessageManager().sendMessage(player, "&7Number: &e" + extension.getConfigManager().getInt("test.number"));
        extension.getMessageManager().sendMessage(player, "&7Boolean: &e" + extension.getConfigManager().getBoolean("test.boolean"));
    }
}
