package me.smegasberla.apexStaff.commands;

import me.smegasberla.apexStaff.ApexStaff;
import me.smegasberla.apexStaff.managers.DatabaseManager;
import me.smegasberla.apexStaff.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class NotesCommand implements CommandExecutor, TabCompleter {

    private final ApexStaff plugin;
    private final DatabaseManager databaseManager;

    public NotesCommand(ApexStaff plugin, DatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NonNull @NotNull String[] args) {

        String wrongArgs = MessageUtils.getMessage(plugin, "wrong-arguments");
        String onlyPlayers = MessageUtils.getMessage(plugin, "only-players");

        if (!(sender instanceof Player)) {
            if (onlyPlayers != null) {

                sender.sendMessage(onlyPlayers);

            }
        }

        Player p = (Player) sender;
        UUID playerUUID = p.getUniqueId();

        if (args.length < 2) {
            if (wrongArgs != null) {
                p.sendMessage(wrongArgs);
                return true;
            }
        }

        String targetName = args[0];
        String subArg = args[1];

        UUID targetUUID = Bukkit.getOfflinePlayer(targetName).getUniqueId();

        if(targetName.equalsIgnoreCase(p.getName())) {

            String message = MessageUtils.getMessage(plugin, "note-yourself");
            if (message != null) {

                p.sendMessage(message);
                return true;
            }

        }

        switch (subArg) {

            case "add":

                if (args.length < 3) {
                    p.sendMessage(ChatColor.RED + "Usage: /ax note <player> add <message>");
                    break;
                }

                String addedNoteMessage = MessageUtils.getMessage(plugin, "added-note", "{target}", targetName);
                StringBuilder noteBuilder = new StringBuilder();

                for (int i = 2; i < args.length; i++) {
                    noteBuilder.append(args[i]);

                    if (i < args.length - 1) {
                        noteBuilder.append(" ");
                    }
                }

                String finalNote = noteBuilder.toString();

                databaseManager.addNote(targetUUID, playerUUID, p.getName(), finalNote);
                if (addedNoteMessage != null) {

                    p.sendMessage(addedNoteMessage);


                }
                break;

            case "remove":
                if (args.length < 3) {
                    p.sendMessage(ChatColor.RED + "Usage: /ax note <player> remove <id>");
                    break;
                }

                try {
                    int id = Integer.parseInt(args[2]);
                    databaseManager.removeNote(id, targetUUID);

                    String removedMsg = MessageUtils.getMessage(plugin, "removed-note", "{target}", targetName);
                    if (removedMsg != null) p.sendMessage(removedMsg);
                } catch (NumberFormatException e) {
                    p.sendMessage(ChatColor.RED + "Error: The ID must be a number!");
                }
                break;

            case "clear":
                String clearedNotes = MessageUtils.getMessage(plugin, "clear-all-notes", "{target}", targetName);

                databaseManager.clearPlayerNotes(targetUUID);
                if (clearedNotes != null) {

                    p.sendMessage(clearedNotes);


                }
                break;

            case "list":

                List<String> noteList = databaseManager.getNotes(targetUUID, targetName);
                String noNotesFound = MessageUtils.getMessage(plugin, "no-notes-found", "{target}", targetName);

                if (noteList.isEmpty()) {
                    p.sendMessage(noNotesFound);
                    break;
                }

                String notesAsText = String.join("\n", noteList);

                String listNotesMessage = MessageUtils.getMessage(plugin, "list-note-message",

                        "{target}", targetName,
                        "{notes_list}", ChatColor.translateAlternateColorCodes('&', notesAsText)

                );

                if (listNotesMessage != null) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', listNotesMessage));
                }

                break;


        }


        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            String partial = args[0].toLowerCase();
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                String name = onlinePlayer.getName();
                if (name.toLowerCase().startsWith(partial)) {
                    completions.add(name);
                }
            }
        } else if (args.length == 2) {
            String partial = args[1].toLowerCase();
            List<String> noteSubCommands = Arrays.asList("add", "remove", "clear", "list");
            for (String noteCmd : noteSubCommands) {
                if (noteCmd.startsWith(partial)) {
                    completions.add(noteCmd);
                }
            }
        }

        return completions;
    }

}