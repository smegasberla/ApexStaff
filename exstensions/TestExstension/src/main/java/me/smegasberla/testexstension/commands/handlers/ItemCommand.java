package me.smegasberla.testexstension.commands.handlers;

import me.smegasberla.testexstension.TestExstension;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

public class ItemCommand {

    private final TestExstension extension;

    public ItemCommand(TestExstension extension) {
        this.extension = extension;
    }

    public void execute(Player player) {
        ItemStack item = extension.getItemManager().createItem(Material.DIAMOND, 1);
        item = extension.getItemManager().setName(item, "&6Test Item");
        item = extension.getItemManager().setLore(item, "&7This is a test item created by TestExstension", "&eLine 2", "&bLine 3");
        item = extension.getItemManager().addEnchantment(item, Enchantment.UNBREAKING, 1);
        extension.getItemManager().giveItem(player, item);
        extension.getMessageManager().sendMessage(player, "&eTest item given!");
    }
}
