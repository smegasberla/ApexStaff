package me.smegasberla.testexstension.commands.handlers;

import me.smegasberla.testexstension.TestExstension;
import org.bukkit.entity.Player;

public class HelpCommand {

    private final TestExstension extension;

    public HelpCommand(TestExstension extension) {
        this.extension = extension;
    }

    public void execute(Player player) {
        extension.getMessageManager().sendMessage(player, "&8--- &6TestExstension Help &8---");
        extension.getMessageManager().sendMessage(player, "&e/testext help &7- Show this help");
        extension.getMessageManager().sendMessage(player, "&e/testext info &7- Show extension info");
        extension.getMessageManager().sendMessage(player, "&e/testext item &7- Give test item");
        extension.getMessageManager().sendMessage(player, "&e/testext message &7- Test message system");
        extension.getMessageManager().sendMessage(player, "&e/testext task &7- Test task scheduler");
        extension.getMessageManager().sendMessage(player, "&e/testext world &7- Test world manager");
        extension.getMessageManager().sendMessage(player, "&e/testext location &7- Test location manager");
        extension.getMessageManager().sendMessage(player, "&e/testext scoreboard &7- Test scoreboard");
        extension.getMessageManager().sendMessage(player, "&e/testext gui &7- Test GUI manager");
        extension.getMessageManager().sendMessage(player, "&e/testext permission &7- Test permission manager");
        extension.getMessageManager().sendMessage(player, "&e/testext config &7- Test config manager");
        extension.getMessageManager().sendMessage(player, "&e/testext database &7- Test database manager");
        extension.getMessageManager().sendMessage(player, "&e/testext placeholder &7- Test placeholder manager");
        extension.getMessageManager().sendMessage(player, "&e/testext metrics &7- Test metrics manager");
        extension.getMessageManager().sendMessage(player, "&e/testext update &7- Test update manager");
        extension.getMessageManager().sendMessage(player, "&e/testext nms &7- Test NMS manager");
        extension.getMessageManager().sendMessage(player, "&e/testext event &7- Test event registration");
        extension.getMessageManager().sendMessage(player, "&e/testext player &7- Test player utils");
    }
}
