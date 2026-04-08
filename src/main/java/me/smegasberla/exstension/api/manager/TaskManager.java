package me.smegasberla.exstension.api.manager;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class TaskManager {

    private final JavaPlugin plugin;
    private final List<BukkitTask> tasks;

    public TaskManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.tasks = new ArrayList<>();
    }

    public BukkitTask runTask(Runnable runnable) {
        BukkitTask task = Bukkit.getScheduler().runTask(plugin, runnable);
        tasks.add(task);
        return task;
    }

    public BukkitTask runTaskLater(Runnable runnable, long delay) {
        BukkitTask task = Bukkit.getScheduler().runTaskLater(plugin, runnable, delay);
        tasks.add(task);
        return task;
    }

    public BukkitTask runTaskTimer(Runnable runnable, long delay, long period) {
        BukkitTask task = Bukkit.getScheduler().runTaskTimer(plugin, runnable, delay, period);
        tasks.add(task);
        return task;
    }

    public BukkitTask runTaskAsynchronously(Runnable runnable) {
        BukkitTask task = Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
        tasks.add(task);
        return task;
    }

    public BukkitTask runTaskLaterAsynchronously(Runnable runnable, long delay) {
        BukkitTask task = Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, runnable, delay);
        tasks.add(task);
        return task;
    }

    public BukkitTask runTaskTimerAsynchronously(Runnable runnable, long delay, long period) {
        BukkitTask task = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, runnable, delay, period);
        tasks.add(task);
        return task;
    }

    public void cancelTask(BukkitTask task) {
        if (task != null) {
            task.cancel();
            tasks.remove(task);
        }
    }

    public void cancelAllTasks() {
        for (BukkitTask task : tasks) {
            if (task != null) {
                task.cancel();
            }
        }
        tasks.clear();
    }

    public int getActiveTaskCount() {
        return tasks.size();
    }

    public boolean isTaskActive(BukkitTask task) {
        return task != null && !task.isCancelled();
    }
}
