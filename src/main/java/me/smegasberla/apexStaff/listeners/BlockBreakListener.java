package me.smegasberla.apexStaff.listeners;

import me.smegasberla.apexStaff.ApexStaff;
import me.smegasberla.apexStaff.enums.MiningBlock;
import me.smegasberla.apexStaff.managers.XRayCheckManager;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.List;
import java.util.UUID;

public class BlockBreakListener implements Listener {

    private final ApexStaff plugin;

    public BlockBreakListener(ApexStaff plugin, XRayCheckManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }

    private final XRayCheckManager manager;



    @EventHandler
    public void onPlayerBreak(BlockBreakEvent e) {

        if(plugin.getConfig().getBoolean("xray.enabled") == true) {

            List<String> materialList = manager.getMaterialList();

            Player p = e.getPlayer();


            UUID playerUUID = p.getUniqueId();

            if(p.hasPermission("apexstaff.exempt")) return;

            Material blockMaterial = e.getBlock().getType();

            if(MiningBlock.isMiningBlock(blockMaterial)) {

                int currentCount = manager.totalBlocks.getOrDefault(playerUUID, 0);
                manager.totalBlocks.put(playerUUID, currentCount + 1);

            }

            if(materialList.contains(blockMaterial.toString())) {

                int currentOreCount = manager.totalOres.getOrDefault(playerUUID, 0);
                manager.totalOres.put(playerUUID, currentOreCount + 1);

            }


        }

    }

}
