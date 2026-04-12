package me.smegasberla.apexStaff.listeners;

import me.smegasberla.apexStaff.ApexStaff;
import me.smegasberla.apexStaff.managers.FreezeManager;
import me.smegasberla.apexStaff.utils.MessageUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.List;
import java.util.UUID;


public class ChatListener implements Listener {

    private final ApexStaff plugin;

    public ChatListener(ApexStaff plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();

        if (!plugin.getConfig().getBoolean("freeze.block-chat-message")) return;
        if (!FreezeManager.isFreezed(p)) return;

        e.setCancelled(true);
        p.sendMessage(MessageUtils.getMessage(plugin, "chat-disallowed"));
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();

        if (!plugin.getConfig().getBoolean("freeze.block-command-execution")) return;
        if (!FreezeManager.isFreezed(p)) return;

        String baseCommand = e.getMessage().substring(1).split(" ")[0];
        List<String> whitelistedCommands = plugin.getConfig().getStringList("freeze.whitelisted-commands");

        if (whitelistedCommands.contains(baseCommand)) return;

        e.setCancelled(true);
        p.sendMessage(MessageUtils.getMessage(plugin, "commands-disallowed"));
    }
}
