package me.smegasberla.exstension.api.manager;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemManager {

    public ItemStack createItem(Material material) {
        return new ItemStack(material);
    }

    public ItemStack createItem(Material material, int amount) {
        return new ItemStack(material, amount);
    }

    public ItemStack createItem(Material material, int amount, short damage) {
        ItemStack item = new ItemStack(material, amount);
        item.setDurability(damage);
        return item;
    }

    public ItemStack setName(ItemStack item, String name) {
        if (item == null) return null;
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            item.setItemMeta(meta);
        }
        return item;
    }

    public ItemStack setLore(ItemStack item, String... lore) {
        if (item == null) return null;
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setLore(Arrays.asList(lore));
            item.setItemMeta(meta);
        }
        return item;
    }

    public ItemStack addEnchantment(ItemStack item, Enchantment enchantment, int level) {
        if (item == null) return null;
        item.addUnsafeEnchantment(enchantment, level);
        return item;
    }

    public ItemStack setAmount(ItemStack item, int amount) {
        if (item == null) return null;
        item.setAmount(amount);
        return item;
    }

    public ItemStack setDurability(ItemStack item, short durability) {
        if (item == null) return null;
        item.setDurability(durability);
        return item;
    }

    public ItemStack setLeatherColor(ItemStack item, Color color) {
        if (item == null) return null;
        if (item.getType().name().contains("LEATHER_")) {
            LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
            if (meta != null) {
                meta.setColor(color);
                item.setItemMeta(meta);
            }
        }
        return item;
    }

    public boolean isSimilar(ItemStack item1, ItemStack item2) {
        if (item1 == null || item2 == null) return false;
        return item1.isSimilar(item2);
    }

    public Material getMaterial(String name) {
        try {
            return Material.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public void giveItem(Player player, ItemStack item) {
        player.getInventory().addItem(item);
    }

    public boolean hasItem(Player player, Material material) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == material) {
                return true;
            }
        }
        return false;
    }

    public int getItemCount(Player player, Material material) {
        int count = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == material) {
                count += item.getAmount();
            }
        }
        return count;
    }

    public void removeItem(Player player, Material material, int amount) {
        int removed = 0;
        for (int i = 0; i < player.getInventory().getContents().length; i++) {
            ItemStack item = player.getInventory().getItem(i);
            if (item != null && item.getType() == material) {
                int itemAmount = item.getAmount();
                if (removed + itemAmount <= amount) {
                    player.getInventory().setItem(i, null);
                    removed += itemAmount;
                } else {
                    int toRemove = amount - removed;
                    item.setAmount(itemAmount - toRemove);
                    player.getInventory().setItem(i, item);
                    break;
                }
            }
            if (removed >= amount) break;
        }
    }
}
