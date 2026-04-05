package me.smegasberla.apexStaff.commands;

import me.smegasberla.apexStaff.ApexStaff;
import me.smegasberla.apexStaff.managers.StaffChatManager;
import me.smegasberla.apexStaff.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.UUID;

public class StaffChatCommand implements CommandExecutor {

    private final ApexStaff plugin;
    private final StaffChatManager manager;

    public StaffChatCommand(ApexStaff plugin, StaffChatManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NonNull @NotNull String[] args) {

        String wrongArgs = MessageUtils.getMessage(plugin, "wrong-arguments");
        String onlyPlayers = MessageUtils.getMessage(plugin, "only-players");


        if (!(sender instanceof Player)) return true;

        Player p = (Player) sender;

        if (args.length > 0) {

            p.sendMessage(wrongArgs);
            return true;
        }

        UUID pUUID = p.getUniqueId();

        manager.toggleStaffChat(pUUID);

        return true;
    }
}
