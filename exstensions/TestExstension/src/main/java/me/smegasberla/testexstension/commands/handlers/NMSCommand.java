package me.smegasberla.testexstension.commands.handlers;

import me.smegasberla.testexstension.TestExstension;
import org.bukkit.entity.Player;

public class NMSCommand {

    private final TestExstension extension;

    public NMSCommand(TestExstension extension) {
        this.extension = extension;
    }

    public void execute(Player player) {
        extension.getMessageManager().sendMessage(player, "&eNMS Info:");
        extension.getMessageManager().sendMessage(player, "&7CraftBukkit Version: &e" + extension.getNMSManager().getCraftBukkitVersion());
        extension.getMessageManager().sendMessage(player, "&7Bukkit Version: &e" + extension.getNMSManager().getBukkitVersion());
        extension.getMessageManager().sendMessage(player, "&7Server Version: &e" + extension.getNMSManager().getServerVersion());
    }
}
