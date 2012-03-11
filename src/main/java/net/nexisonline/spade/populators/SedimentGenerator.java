package net.nexisonline.spade.populators;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.nexisonline.spade.SpadeConf;
import net.nexisonline.spade.SpadeLogging;
import net.nexisonline.spade.SpadePlugin;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;

/**
 * Converted from MineEdit
 * @author Rob
 * 
 */
public class SedimentGenerator extends SpadeEffectGenerator {
    
    private final int waterHeight;
    
    public SedimentGenerator(final SpadePlugin plugin, final Map<String, Object> node, final long seed) {
        super(plugin, node, seed);
        waterHeight = SpadeConf.getInt(node, "water-height", 63);
    }
    
    public static SpadeEffectGenerator getInstance(final SpadePlugin plugin, final Map<String, Object> n, final long seed) {
        final Map<String, Object> node = n;
        return new SedimentGenerator(plugin, node, seed);
    }
    
    @Override
    public Map<String, Object> getConfiguration() {
        final Map<String, Object> cfg = new HashMap<String, Object>();
        cfg.put("water-height", waterHeight);
        return cfg;
    }
    
    @Override
    public void populate(final World world, final Random rand, final Chunk chunk) {
        SpadeLogging.info(String.format("Generating sediment in chunk (%d,%d)", chunk.getX(), chunk.getZ()));
        final int YH = 128;
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                boolean HavePloppedGrass = false;
                for (int y = 127; y > 0; y--) {
                    final byte supportBlock = (byte) chunk.getBlock(x, y - 1, z).getTypeId();
                    byte thisblock = (byte) chunk.getBlock(x, y, z).getTypeId();
                    // Ensure there's going to be stuff holding us up.
                    if ((thisblock == Material.STONE.getId()) && (supportBlock == Material.STONE.getId())) {
                        final int depth = 6;
                        if ((y + depth) >= YH) {
                            continue;
                        }
                        final int ddt = chunk.getBlock(x, y + depth, z).getTypeId();
                        final Biome bt = chunk.getBlock(x, y, z).getBiome();
                        switch (ddt) {
                            case 0: // Air
                            case 8: // Water
                            case 9: // Water
                                if (bt == Biome.TUNDRA) {
                                    thisblock = (byte) Material.SAND.getId();
                                } else {
                                    if ((y - depth) <= waterHeight) {
                                        if (((bt == Biome.TAIGA) || (bt == Biome.SEASONAL_FOREST) || (bt == Biome.TUNDRA)) && (y > waterHeight)) {
                                            thisblock = (byte) ((HavePloppedGrass) ? Material.DIRT.getId() : Material.GRASS.getId());
                                        } else {
                                            thisblock = (byte) (Material.SAND.getId());
                                        }
                                    } else {
                                        thisblock = (byte) ((HavePloppedGrass) ? Material.DIRT.getId() : Material.GRASS.getId());
                                    }
                                }
                                chunk.getBlock(x, y, z).setTypeId(thisblock);
                                if (!HavePloppedGrass) {
                                    HavePloppedGrass = true;
                                }
                                break;
                            default:
                                y = 0;
                                break;
                        }
                    }
                }
            }
        }
    }
}
