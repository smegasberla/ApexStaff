package me.smegasberla.apexStaff.commands;

import me.smegasberla.apexStaff.ApexStaff;
import me.smegasberla.apexStaff.utils.MessageUtils;
import me.smegasberla.exstension.loader.ExstensionLoader;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand implements CommandExecutor {

@Override
public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

ApexStaff plugin = ApexStaff.getPlugin();

if (!(sender instanceof Player)) {
sender.sendMessage("Only players can use this command.");
return true;
}

Player p = (Player) sender;

plugin.reloadConfig();

ExstensionLoader.disableAll();
ExstensionLoader.load(plugin);
ExstensionLoader.enableAll(plugin);

String reloadedMessage = MessageUtils.getMessage(plugin, "successfully-reloaded");

if (reloadedMessage != null) {
p.sendMessage(reloadedMessage);
}

return true;
}
}
