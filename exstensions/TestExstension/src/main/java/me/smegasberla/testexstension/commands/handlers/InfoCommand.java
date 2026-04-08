package me.smegasberla.testexstension.commands.handlers;

import me.smegasberla.testexstension.TestExstension;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class InfoCommand {

    private final TestExstension extension;

    public InfoCommand(TestExstension extension) {
        this.extension = extension;
    }

    public void execute(Player player) {
        extension.getMessageManager().sendMessage(player, "&8--- &6TestExstension Info &8---");
        extension.getMessageManager().sendMessage(player, "&7Extension Name: &e" + extension.getName());
        extension.getMessageManager().sendMessage(player, "&7Extension Version: &e" + extension.getVersion());
        extension.getMessageManager().sendMessage(player, "&7Server Version: &e" + Bukkit.getVersion());
        extension.getMessageManager().sendMessage(player, "&7Online Players: &e" + extension.getPlayerManager().getOnlinePlayerCount());
        extension.getMessageManager().sendMessage(player, "&7Worlds Loaded: &e" + extension.getWorldManager().getWorlds().size());
        extension.getMessageManager().sendMessage(player, "&7Data Folder: &e" + extension.getDataFolder().getPath());
    }
}
