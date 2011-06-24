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

	public void addToProtochunk(byte[][][] blocks, int X, int Z,Biome[][] biomes) {
        int YH = 128;
        for (int x = 0; x < 16; x++)
        {
            for (int z = 0; z < 16; z++)
            {
            	
                int H=Math.max(Math.min(topBlockY(blocks, x, z),127),16);
                int nextH=0;
                if(x!=15)
                	nextH=Math.max(Math.min(topBlockY(blocks, x+1, z),127),16);
                else 
                	nextH=Math.max(Math.min(topBlockY(blocks, x-1, z),127),16);
                
                boolean HavePloppedGrass = false;
                for (int y = 127; y > 0; y--)
                {
                	byte supportBlock = blocks[x][y-1][z];
                    byte thisblock = blocks[x][y][z];
                    // Ensure there's going to be stuff holding us up.
                    if (thisblock == Material.STONE.getId() 
                    	&& supportBlock==Material.STONE.getId())
                    {
                    	int depth= H/nextH;
                        if (y + depth >= YH)
                            continue;
                        int ddt = blocks[x][y+depth][z];
                        Biome bt = biomes[x][z];
                        switch (ddt)
                        {
                            case 0: // Air
                            case 8: // Water
                            case 9: // Water
                                if (bt == Biome.TUNDRA)
                                {
                                	blocks[x][y][z]=(byte) Material.SAND.getId();
                                }
                                else
                                {
                                    if (y - depth <= waterHeight)
                                    {
                                        if ((bt == Biome.TAIGA || bt == Biome.SEASONAL_FOREST || bt == Biome.TUNDRA) && y > waterHeight)
                                        {
                                        	blocks[x][y][z]=(byte) ((HavePloppedGrass) ? Material.DIRT.getId() : Material.GRASS.getId());
                                        }
                                        else
                                        {
                                        	blocks[x][y][z]=(byte) (Material.SAND.getId());
                                        }
                                    }
                                    else
                                    	blocks[x][y][z]= (byte) ((HavePloppedGrass) ? Material.DIRT.getId() : Material.GRASS.getId());
                                }
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

	private int topBlockY(byte[][][] blocks, int x, int z) {
		int y = 127;
		for(; y>0 && !blockIsSolid(blocks[x][y][z]); --y) {}
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
	public void populate(World arg0, Random arg1, Chunk arg2) {
		// TODO Auto-generated method stub
		
	}
}
