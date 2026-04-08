package me.smegasberla.testexstension.commands;

import me.smegasberla.testexstension.TestExstension;
import me.smegasberla.testexstension.commands.handlers.*;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class TestExtensionCommand implements CommandExecutor {

    private final TestExstension extension;
    private final HelpCommand helpCommand;
    private final InfoCommand infoCommand;
    private final ItemCommand itemCommand;
    private final MessageCommand messageCommand;
    private final TaskCommand taskCommand;
    private final WorldCommand worldCommand;
    private final LocationCommand locationCommand;
    private final ScoreboardCommand scoreboardCommand;
    private final GUICommand guiCommand;
    private final PermissionCommand permissionCommand;
    private final ConfigCommand configCommand;
    private final DatabaseCommand databaseCommand;
    private final PlaceholderCommand placeholderCommand;
    private final MetricsCommand metricsCommand;
    private final UpdateCommand updateCommand;
    private final NMSCommand nmsCommand;
    private final EventCommand eventCommand;
    private final PlayerCommand playerCommand;

    public TestExtensionCommand(TestExstension extension) {
        this.extension = extension;
        this.helpCommand = new HelpCommand(extension);
        this.infoCommand = new InfoCommand(extension);
        this.itemCommand = new ItemCommand(extension);
        this.messageCommand = new MessageCommand(extension);
        this.taskCommand = new TaskCommand(extension);
        this.worldCommand = new WorldCommand(extension);
        this.locationCommand = new LocationCommand(extension);
        this.scoreboardCommand = new ScoreboardCommand(extension);
        this.guiCommand = new GUICommand(extension);
        this.permissionCommand = new PermissionCommand(extension);
        this.configCommand = new ConfigCommand(extension);
        this.databaseCommand = new DatabaseCommand(extension);
        this.placeholderCommand = new PlaceholderCommand(extension);
        this.metricsCommand = new MetricsCommand(extension);
        this.updateCommand = new UpdateCommand(extension);
        this.nmsCommand = new NMSCommand(extension);
        this.eventCommand = new EventCommand(extension);
        this.playerCommand = new PlayerCommand(extension);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players!");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("testexstension.use")) {
            extension.getMessageManager().sendMessage(player, "&cYou don't have permission to use this command!");
            return true;
        }

        if (args.length == 0) {
            helpCommand.execute(player);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "help":
                helpCommand.execute(player);
                break;
            case "info":
                infoCommand.execute(player);
                break;
            case "item":
                itemCommand.execute(player);
                break;
            case "message":
                messageCommand.execute(player);
                break;
            case "task":
                taskCommand.execute(player);
                break;
            case "world":
                worldCommand.execute(player);
                break;
            case "location":
                locationCommand.execute(player);
                break;
            case "scoreboard":
                scoreboardCommand.execute(player);
                break;
            case "gui":
                guiCommand.execute(player);
                break;
            case "permission":
                permissionCommand.execute(player);
                break;
            case "config":
                configCommand.execute(player);
                break;
            case "database":
                databaseCommand.execute(player);
                break;
            case "placeholder":
                placeholderCommand.execute(player);
                break;
            case "metrics":
                metricsCommand.execute(player);
                break;
            case "update":
                updateCommand.execute(player);
                break;
            case "nms":
                nmsCommand.execute(player);
                break;
            case "event":
                eventCommand.execute(player);
                break;
            case "player":
                playerCommand.execute(player);
                break;
            default:
                helpCommand.execute(player);
                break;
        }

        return true;
    }
}
