package net.nexisonline.spade.populators;

import java.util.Random;

import net.nexisonline.spade.SpadePlugin;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.util.config.Configuration;
import org.bukkit.util.config.ConfigurationNode;

/**
 * Converted from MineEdit
 * @author Rob
 *
 */
public class SedimentGenerator extends SpadeEffectGenerator {

	private int waterHeight;

	public SedimentGenerator(SpadePlugin plugin, ConfigurationNode node, long seed) {
		super(plugin, node, seed);
		waterHeight = node.getInt("water-height", 63);
	}

	private int topBlockY(Chunk blocks, int x, int z) {
		int y = 127;
		for(; y>0 && !blockIsSolid((byte) blocks.getBlock(x,y,z).getTypeId()); --y) {}
		return y;
	}

	private boolean blockIsSolid(byte b) {
		Material mat = Material.getMaterial(b);
		return mat!=Material.AIR && mat!=Material.WATER && mat!=Material.STATIONARY_WATER && mat!=Material.LAVA && mat!=Material.STATIONARY_LAVA;
	}

	@Override
	public ConfigurationNode getConfiguration() {
		ConfigurationNode cfg = Configuration.getEmptyNode();
		cfg.setProperty("water-height",waterHeight);
		return cfg;
	}

	@Override
	public void populate(World world, Random rand, Chunk chunk) {
		int YH = 128;
        for (int x = 0; x < 16; x++)
        {
            for (int z = 0; z < 16; z++)
            {
                int H=Math.max(Math.min(topBlockY(chunk, x, z),127),16);
                int nextH=0;
                if(x!=15)
                	nextH=Math.max(Math.min(topBlockY(chunk, x+1, z),127),16);
                else 
                	nextH=Math.max(Math.min(topBlockY(chunk, x-1, z),127),16);
                
                boolean HavePloppedGrass = false;
                for (int y = 127; y > 0; y--)
                {
                	byte supportBlock = (byte) chunk.getBlock(x, y-1, z).getTypeId();
                    byte thisblock = (byte) chunk.getBlock(x, y, z).getTypeId();
                    // Ensure there's going to be stuff holding us up.
                    if (thisblock == Material.STONE.getId() 
                    	&& supportBlock==Material.STONE.getId())
                    {
                    	int depth= H/nextH;
                        if (y + depth >= YH)
                            continue;
                        int ddt = chunk.getBlock(x, y+depth, z).getTypeId();
                        Biome bt = Biome.RAINFOREST; // TODO Figure out how to get a biome.
                        switch (ddt)
                        {
                            case 0: // Air
                            case 8: // Water
                            case 9: // Water
                                if (bt == Biome.TUNDRA)
                                {
                                	thisblock=(byte) Material.SAND.getId();
                                }
                                else
                                {
                                    if (y - depth <= waterHeight)
                                    {
                                        if ((bt == Biome.TAIGA || bt == Biome.SEASONAL_FOREST || bt == Biome.TUNDRA) && y > waterHeight)
                                        {
                                        	thisblock=(byte) ((HavePloppedGrass) ? Material.DIRT.getId() : Material.GRASS.getId());
                                        }
                                        else
                                        {
                                        	thisblock=(byte) (Material.SAND.getId());
                                        }
                                    }
                                    else
                                    	thisblock= (byte) ((HavePloppedGrass) ? Material.DIRT.getId() : Material.GRASS.getId());
                                }
                                chunk.getBlock(x, y, z).setTypeId(thisblock);
                                if (!HavePloppedGrass)
                                    HavePloppedGrass = true;
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
