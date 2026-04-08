package me.smegasberla.testexstension.commands.handlers;

import me.smegasberla.testexstension.TestExstension;
import org.bukkit.entity.Player;

public class WorldCommand {

    private final TestExstension extension;

    public WorldCommand(TestExstension extension) {
        this.extension = extension;
    }

    public void execute(Player player) {
        extension.getMessageManager().sendMessage(player, "&eWorld Info:");
        extension.getMessageManager().sendMessage(player, "&7Current World: &e" + player.getWorld().getName());
        extension.getMessageManager().sendMessage(player, "&7Loaded Worlds: &e" + extension.getWorldManager().getWorlds().size());
        extension.getMessageManager().sendMessage(player, "&7World Time: &e" + player.getWorld().getTime());
        extension.getMessageManager().sendMessage(player, "&7Is Day Time: &e" + extension.getWorldManager().isDayTime(player.getWorld()));
        extension.getMessageManager().sendMessage(player, "&7Chunk Count: &e" + extension.getWorldManager().getChunkCount(player.getWorld()));
        extension.getWorldManager().setDayTime(player.getWorld());
        extension.getMessageManager().sendMessage(player, "&eSet to day time!");
    }
}
