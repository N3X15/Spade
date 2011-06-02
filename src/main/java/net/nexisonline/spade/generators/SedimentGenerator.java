package net.nexisonline.spade.generators;

import net.nexisonline.spade.SpadePlugin;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.util.config.ConfigurationNode;

/**
 * Converted from MineEdit
 * @author Rob
 *
 */
public class SedimentGenerator extends SpadeEffectGenerator {

	private int waterHeight;

	public SedimentGenerator(SpadePlugin plugin, World w,
			ConfigurationNode node, long seed) {
		super(plugin, w, node, seed);
		waterHeight = node.getInt("waterheight", 63);
	}

	@Override
	public void addToProtochunk(byte[][][] blocks, int X, int Z,Biome[][] biomes) {
        int YH = 128;
        int xo = X * 16;
        int zo = Z * 16;
        for (int x = 0; x < 16; x++)
        {
            for (int z = 0; z < 16; z++)
            {
                int H=Math.max(Math.min(world.getHighestBlockYAt(x+xo, z+zo),128),16);
                int nextH=0;
                if(x==127)
                	nextH=Math.max(Math.min(world.getHighestBlockYAt(x+1+xo, z+zo),128),16);
                else 
                	nextH=Math.max(Math.min(world.getHighestBlockYAt(x-1+xo, z+zo),128),16);
                
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

	@Override
	public void addToChunk(Chunk chunk, int x, int z) {
		// TODO Auto-generated method stub
		
	}

}
