package me.smegasberla.apexStaff.enums;

import org.bukkit.Material;
import java.util.HashSet;
import java.util.Set;

public enum MiningBlock {
    STONE(Material.STONE),
    GRANITE(Material.GRANITE),
    DIORITE(Material.DIORITE),
    ANDESITE(Material.ANDESITE),
    TUFF(Material.TUFF),
    DIRT(Material.DIRT),
    GRAVEL(Material.GRAVEL),
    SAND(Material.SAND),

    DEEPSLATE(Material.DEEPSLATE),
    COBBLED_DEEPSLATE(Material.COBBLED_DEEPSLATE),

    NETHERRACK(Material.NETHERRACK),
    BLACKSTONE(Material.BLACKSTONE),
    BASALT(Material.BASALT),
    SOUL_SAND(Material.SOUL_SAND),
    SOUL_SOIL(Material.SOUL_SOIL),
    MAGMA_BLOCK(Material.MAGMA_BLOCK);

    private final Material material;

    MiningBlock(Material material) {
        this.material = material;
    }

    public Material getMaterial() {
        return material;
    }

    private static final Set<Material> MINING_MATERIALS = new HashSet<>();

    static {
        for (MiningBlock mb : values()) {
            MINING_MATERIALS.add(mb.getMaterial());
        }
    }

    public static boolean isMiningBlock(Material material) {
        return MINING_MATERIALS.contains(material);
    }
}