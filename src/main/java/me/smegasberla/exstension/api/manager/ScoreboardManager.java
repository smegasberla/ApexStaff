package me.smegasberla.exstension.api.manager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ScoreboardManager {

    private final Map<UUID, Scoreboard> playerScoreboards;
    private final Map<String, Objective> objectives;

    public ScoreboardManager() {
        this.playerScoreboards = new HashMap<>();
        this.objectives = new HashMap<>();
    }

public Scoreboard createScoreboard(Player player) {
Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
playerScoreboards.put(player.getUniqueId(), scoreboard);
return scoreboard;
}

    public Scoreboard getScoreboard(Player player) {
        return playerScoreboards.get(player.getUniqueId());
    }

    public void setScoreboard(Player player, Scoreboard scoreboard) {
        playerScoreboards.put(player.getUniqueId(), scoreboard);
        player.setScoreboard(scoreboard);
    }

    public void removeScoreboard(Player player) {
        playerScoreboards.remove(player.getUniqueId());
    }

    public Objective createObjective(String name, String displayName, String criteria) {
        Objective objective = Bukkit.getScoreboardManager().getMainScoreboard().registerNewObjective(name, criteria, displayName);
        objectives.put(name, objective);
        return objective;
    }

    public Objective getObjective(String name) {
        return objectives.get(name);
    }

    public void removeObjective(String name) {
        Objective objective = objectives.remove(name);
        if (objective != null) {
            objective.unregister();
        }
    }

    public void setLine(Player player, String objectiveName, String text, int position) {
        Scoreboard scoreboard = getScoreboard(player);
        if (scoreboard == null) {
            scoreboard = createScoreboard(player);
        }

        Objective objective = getObjective(objectiveName);
        if (objective == null) {
            objective = createObjective(objectiveName, objectiveName, "dummy");
        }

        String entry = "line" + position;
        objective.getScore(entry).setScore(position);
    }

    public void clearScoreboard(Player player) {
        Scoreboard scoreboard = getScoreboard(player);
        if (scoreboard != null) {
            for (Objective objective : scoreboard.getObjectives()) {
                objective.unregister();
            }
        }
        removeScoreboard(player);
    }

    public int getScoreboardCount() {
        return playerScoreboards.size();
    }
}
