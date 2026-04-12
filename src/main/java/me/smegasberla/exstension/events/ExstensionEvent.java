package me.smegasberla.exstension.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ExstensionEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final String exstensionName;
    private final ExstensionEventType type;

    public ExstensionEvent(String exstensionName, ExstensionEventType type) {
        this.exstensionName = exstensionName;
        this.type = type;
    }

    public String getExstensionName() {
        return exstensionName;
    }

    public ExstensionEventType getType() {
        return type;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public enum ExstensionEventType {
        LOAD,
        ENABLE,
        DISABLE,
        UNLOAD
    }
}
