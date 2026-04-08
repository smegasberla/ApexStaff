package me.smegasberla.testexstension.commands.handlers;

import me.smegasberla.testexstension.TestExstension;
import org.bukkit.entity.Player;

public class TaskCommand {

    private final TestExstension extension;

    public TaskCommand(TestExstension extension) {
        this.extension = extension;
    }

    public void execute(Player player) {
        extension.getMessageManager().sendMessage(player, "&eScheduling test tasks...");

        extension.getTaskManager().runTask(() -> {
            extension.getMessageManager().sendMessage(player, "&7[Task 1] Immediate task executed!");
        });

        extension.getTaskManager().runTaskLater(() -> {
            extension.getMessageManager().sendMessage(player, "&7[Task 2] Delayed task executed!");
        }, 60L);

        extension.getTaskManager().runTaskTimer(() -> {
            extension.getMessageManager().sendMessage(player, "&7[Task 3] Repeating task tick!");
        }, 100L, 100L);

        extension.getTaskManager().runTaskAsynchronously(() -> {
            extension.getMessageManager().sendMessage(player, "&7[Task 4] Async task executed!");
        });
    }
}
