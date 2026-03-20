package me.smegasberla.apexStaff;

import me.smegasberla.apexStaff.commands.ApexCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class ApexStaff extends JavaPlugin {

    private static ApexStaff plugin;

    @Override
    public void onLoad() {



    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        plugin = this;

        getCommand("apexstaff").setExecutor(new ApexCommand());


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static void sendHelpMessage(CommandSender sender) {
        sender.sendMessage(ChatColor.DARK_GRAY + "=================================");
        sender.sendMessage(ChatColor.GOLD + "        ApexStaff Help Menu");
        sender.sendMessage(ChatColor.DARK_GRAY + "=================================");
        sender.sendMessage("");
        sender.sendMessage(ChatColor.AQUA + "/apexstaff" + ChatColor.GRAY + " - " + ChatColor.WHITE + "Show this help message");
        sender.sendMessage(ChatColor.AQUA + "/apexstaff reload" + ChatColor.GRAY + " - " + ChatColor.WHITE + "Reload the plugin configuration");
        sender.sendMessage(ChatColor.AQUA + "/apexstaff vanish" + ChatColor.GRAY + " - " + ChatColor.WHITE + "Toggle vanish mode");
        sender.sendMessage("");
        sender.sendMessage(ChatColor.DARK_GRAY + "=================================");
    }

    public static ApexStaff getPlugin() {
        return plugin;
    }

}
