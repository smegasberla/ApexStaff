package me.smegasberla.testexstension.commands.handlers;

import me.smegasberla.testexstension.TestExstension;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

public class GUICommand {

    private final TestExstension extension;

    public GUICommand(TestExstension extension) {
        this.extension = extension;
    }

    public void execute(Player player) {
        Inventory gui = extension.getGuiManager().createInventory("&6Test GUI", 27);

        ItemStack item1 = extension.getItemManager().createItem(Material.GOLD_BLOCK, 1);
        extension.getItemManager().setName(item1, "&6Gold Block");
        gui.setItem(10, item1);

        ItemStack item2 = extension.getItemManager().createItem(Material.IRON_BLOCK, 1);
        extension.getItemManager().setName(item2, "&fIron Block");
        gui.setItem(11, item2);

        ItemStack item3 = extension.getItemManager().createItem(Material.DIAMOND_BLOCK, 1);
        extension.getItemManager().setName(item3, "&bDiamond Block");
        gui.setItem(12, item3);

        extension.getGuiManager().setClickHandler(player, event -> {
            event.setCancelled(true);
            if (event.getCurrentItem() != null) {
                extension.getMessageManager().sendMessage(player, "&eYou clicked: " + event.getCurrentItem().getType());
            }
        });

        extension.getGuiManager().openInventory(player, gui);
    }
}
