package me.smegasberla.exstension.api.manager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class GUIManager implements Listener {

    private final JavaPlugin plugin;
    private final Map<UUID, Consumer<InventoryClickEvent>> clickHandlers;
    private final Map<UUID, Consumer<InventoryCloseEvent>> closeHandlers;
    private final Map<UUID, Consumer<InventoryDragEvent>> dragHandlers;

    public GUIManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.clickHandlers = new HashMap<>();
        this.closeHandlers = new HashMap<>();
        this.dragHandlers = new HashMap<>();

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public Inventory createInventory(String title, int size) {
        return Bukkit.createInventory(null, size, title);
    }

    public Inventory createInventory(String title, int size, ItemStack[] contents) {
        Inventory inventory = createInventory(title, size);
        if (contents != null) {
            inventory.setContents(contents);
        }
        return inventory;
    }

    public void setClickHandler(Player player, Consumer<InventoryClickEvent> handler) {
        clickHandlers.put(player.getUniqueId(), handler);
    }

    public void setCloseHandler(Player player, Consumer<InventoryCloseEvent> handler) {
        closeHandlers.put(player.getUniqueId(), handler);
    }

    public void setDragHandler(Player player, Consumer<InventoryDragEvent> handler) {
        dragHandlers.put(player.getUniqueId(), handler);
    }

    public void removeHandlers(Player player) {
        UUID uuid = player.getUniqueId();
        clickHandlers.remove(uuid);
        closeHandlers.remove(uuid);
        dragHandlers.remove(uuid);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        Consumer<InventoryClickEvent> handler = clickHandlers.get(player.getUniqueId());

        if (handler != null) {
            handler.accept(event);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getPlayer();
        Consumer<InventoryCloseEvent> handler = closeHandlers.get(player.getUniqueId());

        if (handler != null) {
            handler.accept(event);
            closeHandlers.remove(player.getUniqueId());
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        Consumer<InventoryDragEvent> handler = dragHandlers.get(player.getUniqueId());

        if (handler != null) {
            handler.accept(event);
        }
    }

    public void openInventory(Player player, Inventory inventory) {
        player.openInventory(inventory);
    }

    public void updateInventory(Inventory inventory, int slot, ItemStack item) {
        inventory.setItem(slot, item);
    }

    public void clearInventory(Inventory inventory) {
        inventory.clear();
    }
}
