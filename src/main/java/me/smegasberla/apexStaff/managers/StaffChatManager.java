package me.smegasberla.apexStaff.managers;

import me.smegasberla.apexStaff.ApexStaff;
import me.smegasberla.apexStaff.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.UUID;

public class StaffChatManager {

    private final ApexStaff plugin;

    public HashSet<UUID> isInStaffChat = new HashSet<>();

    public StaffChatManager(ApexStaff plugin) {
        this.plugin = plugin;
    }

    public void enterStaffChat(UUID playerUUID) {

        Player p = Bukkit.getPlayer(playerUUID);

        String noPermission = ChatColor.translateAlternateColorCodes('&',
        plugin.getConfig().getString("no-permission", "&cNo permission."));


        if(p.hasPermission("apexstaff.staffchat")) {

            isInStaffChat.add(playerUUID);

        } else {

            p.sendMessage(noPermission);

        }



    }

    public void exitStaffChat(UUID playerUUID) {

        isInStaffChat.remove(playerUUID);

    }

    public boolean currentlyInStaffChat(UUID playerUUID) {

        if(isInStaffChat.contains(playerUUID)) {

            return true;

        } else {

            return false;

        }

    }

    public void toggleStaffChat(UUID playerUUID) {

        Player p = Bukkit.getPlayer(playerUUID);

        String enteredStaffChat = MessageUtils.getMessage(plugin, "entered-staff-chat");
        String exitedStaffChat = MessageUtils.getMessage(plugin, "exited-staff-chat");

        if(currentlyInStaffChat(playerUUID)) {

            exitStaffChat(playerUUID);
            if(exitedStaffChat != null) {

                p.sendMessage(exitedStaffChat);

            }

        } else {

            enterStaffChat(playerUUID);
            if(enteredStaffChat != null) {

                p.sendMessage(enteredStaffChat);

            }

        }
    }


}
