package me.smegasberla.testexstension.commands.handlers;

import me.smegasberla.testexstension.TestExstension;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreboardCommand {

    private final TestExstension extension;

    public ScoreboardCommand(TestExstension extension) {
        this.extension = extension;
    }

    public void execute(Player player) {
        Scoreboard scoreboard = extension.getScoreboardManager().createScoreboard(player);
        player.setScoreboard(scoreboard);

        Objective objective = scoreboard.registerNewObjective("testobj", "dummy", "&6Test Objective");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        objective.getScore("&eTest Score 1").setScore(10);
        objective.getScore("&eTest Score 2").setScore(20);
        objective.getScore("&eTest Score 3").setScore(30);

        extension.getMessageManager().sendMessage(player, "&eTest scoreboard created!");
    }
}
