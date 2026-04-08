package me.smegasberla.exstension.api.manager;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandManager {

private final JavaPlugin plugin;
private final Map<String, DynamicCommand> registeredCommands;
private CommandMap commandMap;

public CommandManager(JavaPlugin plugin) {
this.plugin = plugin;
this.registeredCommands = new HashMap<>();
try {
Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
field.setAccessible(true);
commandMap = (CommandMap) field.get(Bukkit.getServer());
} catch (Exception e) {
e.printStackTrace();
}
}

    public boolean registerCommand(String name, CommandExecutor executor) {
        return registerCommand(name, executor, "Extension command", "/" + name, null, null);
    }

    public boolean registerCommand(String name, CommandExecutor executor, String description, String usage, String permission, TabCompleter completer) {
        if (commandMap == null) {
            return false;
        }

        try {
            DynamicCommand command = new DynamicCommand(name, plugin);
            command.setExecutor(executor);
            if (completer != null) {
                command.setTabCompleter(completer);
            }
            command.setDescription(description != null ? description : "Extension command");
            command.setUsage(usage != null ? usage : "/" + name);
            if (permission != null) {
                command.setPermission(permission);
            }

commandMap.register(name, command);
registeredCommands.put(name, command);

return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

public boolean unregisterCommand(String name) {
if (commandMap == null) {
return false;
}

try {
Command command = registeredCommands.get(name);
if (command != null) {
command.unregister(commandMap);
}

return registeredCommands.remove(name) != null;
} catch (Exception e) {
e.printStackTrace();
return false;
}
}

    public CommandExecutor getCommandExecutor(String name) {
        DynamicCommand command = registeredCommands.get(name);
        return command != null ? command.getExecutor() : null;
    }

    public boolean isCommandRegistered(String name) {
        return registeredCommands.containsKey(name);
    }

    public int getRegisteredCommandCount() {
        return registeredCommands.size();
    }

    public void unregisterAll() {
        for (String commandName : registeredCommands.keySet()) {
            unregisterCommand(commandName);
        }
        registeredCommands.clear();
    }

private static class DynamicCommand extends Command {
private CommandExecutor executor;
private TabCompleter tabCompleter;
private final JavaPlugin plugin;

public DynamicCommand(String name, JavaPlugin plugin) {
super(name, "Extension command", "/" + name, new ArrayList<>());
this.executor = null;
this.tabCompleter = null;
this.plugin = plugin;
this.setPermission("apexstaff.admin");
}

@Override
public List<String> getAliases() {
return new ArrayList<>();
}

        @Override
        public boolean execute(org.bukkit.command.CommandSender sender, String commandLabel, String[] args) {
            if (executor != null) {
                return executor.onCommand(sender, this, commandLabel, args);
            }
            return false;
        }

        @Override
        public java.util.List<String> tabComplete(org.bukkit.command.CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
            if (tabCompleter != null) {
                return tabCompleter.onTabComplete(sender, this, alias, args);
            }
            return super.tabComplete(sender, alias, args);
        }

        public void setExecutor(CommandExecutor executor) {
            this.executor = executor;
        }

        public CommandExecutor getExecutor() {
            return executor;
        }

        public void setTabCompleter(TabCompleter tabCompleter) {
            this.tabCompleter = tabCompleter;
        }
    }
}
