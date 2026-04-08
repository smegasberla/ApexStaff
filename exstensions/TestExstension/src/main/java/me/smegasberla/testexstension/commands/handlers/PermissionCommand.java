package me.smegasberla.testexstension.commands.handlers;

import me.smegasberla.testexstension.TestExstension;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

public class PermissionCommand {

    private final TestExstension extension;

    public PermissionCommand(TestExstension extension) {
        this.extension = extension;
    }

    public void execute(Player player) {
        extension.getPermissionManager().registerPermission("testexstension.test", "Test permission", PermissionDefault.TRUE);
        extension.getMessageManager().sendMessage(player, "&eRegistered test permission!");
        extension.getMessageManager().sendMessage(player, "&7Has permission: &e" + extension.getPermissionManager().hasPermission(player, "testexstension.test"));
    }
}
