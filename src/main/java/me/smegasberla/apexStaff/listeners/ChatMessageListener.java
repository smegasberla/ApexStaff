package me.smegasberla.apexStaff.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.smegasberla.apexStaff.managers.StaffChatManager;
import me.smegasberla.apexStaff.utils.MessageUtils;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

public class ChatMessageListener implements Listener {

    private final StaffChatManager manager;
    private final net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer serializer = PlainTextComponentSerializer.plainText();

    public ChatMessageListener(StaffChatManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onPlayerChatMessage(AsyncChatEvent e) {
        Player p = e.getPlayer();
        UUID pUUID = p.getUniqueId();
        String message = serializer.serialize(e.message());

        if (!p.hasPermission("apexstaff.staffchat")) {
            return;
        }

        if (manager.currentlyInStaffChat(pUUID)) {
            e.setCancelled(true);
            String staffChatFormat = "&8[&cSTAFF&8] &7%s: &f%s";
            String formattedMessage = String.format(staffChatFormat, p.getName(), message);
            formattedMessage = ChatColor.translateAlternateColorCodes('&', formattedMessage);
            for (Player online : Bukkit.getOnlinePlayers()) {
                if (manager.currentlyInStaffChat(online.getUniqueId())) {
                    online.sendMessage(formattedMessage);
                }
            }
        } else {
            e.setCancelled(false);
        }
    }
}
