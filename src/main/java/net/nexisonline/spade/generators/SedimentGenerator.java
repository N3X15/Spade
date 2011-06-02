package net.nexisonline.spade.generators;

import net.nexisonline.spade.SpadePlugin;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
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
	public void addToChunk(Chunk chunk, int X, int Z) {
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
                    Block supportBlock = chunk.getBlock(x, y-1, z);
                    Block thisblock = chunk.getBlock(x,y,z);
                    // Ensure there's going to be stuff holding us up.
                    if (thisblock.getType() == Material.STONE 
                    	&& supportBlock.getType()==Material.STONE)
                    {
                    	int depth= H/nextH;
                        if (y + depth >= YH)
                            continue;
                        int ddt = chunk.getBlock(x, y+depth, z).getTypeId();
                        Biome bt = thisblock.getBiome();
                        switch (ddt)
                        {
                            case 0: // Air
                            case 8: // Water
                            case 9: // Water
                                if (bt == Biome.TUNDRA)
                                {
                                    thisblock.setType(Material.SAND);
                                }
                                else
                                {
                                    if (y - depth <= waterHeight)
                                    {
                                        if ((bt == Biome.TAIGA || bt == Biome.SEASONAL_FOREST || bt == Biome.TUNDRA) && y > waterHeight)
                                        {
                                            thisblock.setType((HavePloppedGrass) ? Material.DIRT : Material.GRASS);
                                        }
                                        else
                                        {
                                            thisblock.setType(Material.SAND);
                                        }
                                    }
                                    else
                                    	thisblock.setType((HavePloppedGrass) ? Material.DIRT : Material.GRASS);
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

}
