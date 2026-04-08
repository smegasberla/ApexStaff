package me.smegasberla.testexstension.commands.handlers;

import me.smegasberla.testexstension.TestExstension;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class MessageCommand {

    private final TestExstension extension;

    public MessageCommand(TestExstension extension) {
        this.extension = extension;
    }

    public void execute(Player player) {
        extension.getMessageManager().sendMessage(player, "&eTest message sent!");
        extension.getMessageManager().sendTitle(player, "&6Test Title", "&eTest Subtitle", 10, 70, 20);
        extension.getMessageManager().sendActionBar(player, "&eTest Action Bar Message");
        extension.getMessageManager().playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        extension.getMessageManager().broadcastMessage("&eBroadcast test message!");
    }
}
