package me.smegasberla.testexstension;

import me.smegasberla.exstension.Exstension;
import me.smegasberla.testexstension.commands.TestExtensionCommand;
import me.smegasberla.testexstension.listeners.PlayerListener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class TestExstension extends Exstension {

    private PlayerListener playerListener;

    @Override
    public void onEnable() {
        getLogger().info("TestExstension is enabling!");

        getMessageManager().setPrefix("&8[&6TestExstension&8] &r");

        getPermissionManager().registerPermission("testexstension.admin", "Admin access for TestExstension");
        getPermissionManager().registerPermission("testexstension.use", "Use TestExstension features");

getCommandManager().registerCommand("testext", new TestExtensionCommand(this), "Test extension main command", "/testext [help|info|item|message|task|world|location|scoreboard|gui|permission|config|database|placeholder|metrics|update|nms|event|player]", "testexstension.use", null);
getCommandManager().registerCommand("testextinfo", new TestExtensionCommand(this), "Show test extension info", "/testextinfo", "testexstension.use", null);

        playerListener = new PlayerListener(this);
        getEventManager().registerEvent(PlayerJoinEvent.class, playerListener::onPlayerJoin);
        getEventManager().registerEvent(PlayerQuitEvent.class, playerListener::onPlayerQuit);

        getTaskManager().runTaskLater(() -> {
            getLogger().info("Delayed task executed!");
        }, 100L);

        getTaskManager().runTaskTimer(() -> {
            if (getPlayerManager().hasPlayers()) {
                getLogger().info("Online players: " + getPlayerManager().getOnlinePlayerCount());
            }
        }, 0L, 1200L);

        saveDefaultConfig();
        getConfig().set("enabled", true);
        getConfig().set("message", "Hello from TestExstension!");
        saveConfig();

        getLogger().info("TestExstension has been enabled successfully!");
        getLogger().info("Available commands: /testext, /testextinfo");
    }

    @Override
    public void onDisable() {
        getLogger().info("TestExstension is disabling!");

        if (getConfig() != null) {
            saveConfig();
        }

        getLogger().info("TestExstension has been disabled!");
    }

    @Override
    public String getName() {
        return "TestExstension";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }
}
