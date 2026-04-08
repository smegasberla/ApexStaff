package me.smegasberla.exstension.api.manager;

import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class EventManager {

    private final JavaPlugin plugin;
    private final List<Listener> registeredListeners;

    public EventManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.registeredListeners = new ArrayList<>();
    }

    public <T extends Event> void registerEvent(Class<T> eventClass, EventPriority priority, EventListener<T> listener) {
        Listener eventListener = new Listener() {};
        plugin.getServer().getPluginManager().registerEvent(eventClass, eventListener, priority, (l, event) -> {
            if (eventClass.isInstance(event)) {
                listener.onEvent(eventClass.cast(event));
            }
        }, plugin);
        registeredListeners.add(eventListener);
    }

    public <T extends Event> void registerEvent(Class<T> eventClass, EventListener<T> listener) {
        registerEvent(eventClass, EventPriority.NORMAL, listener);
    }

    public void registerListener(Listener listener) {
        plugin.getServer().getPluginManager().registerEvents(listener, plugin);
        registeredListeners.add(listener);
    }

    public void unregisterAll() {
        for (Listener listener : registeredListeners) {
            HandlerList.unregisterAll(listener);
        }
        registeredListeners.clear();
    }

    public int getRegisteredListenerCount() {
        return registeredListeners.size();
    }

    public interface EventListener<T extends Event> {
        void onEvent(T event);
    }
}
