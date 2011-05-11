package net.nexisonline.spade;

import java.util.ArrayList;

import net.nexisonline.spade.generators.SpadeEffectGenerator;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.util.config.ConfigurationNode;

public class GenerationManager {
	boolean populate=true;
	ArrayList<SpadeEffectGenerator> populators = new ArrayList<SpadeEffectGenerator>();
	private SpadeTerrainGenerator terraingen;
	
	@SuppressWarnings("unchecked")
	public GenerationManager(String world, ConfigurationNode cfg) {
		// Terrain generation
		ConfigurationNode node = cfg.getNode("terrain");
		try {
			Class<? extends SpadeTerrainGenerator> tgc = (Class<? extends SpadeTerrainGenerator>) Class.forName(node.getString("name"));
			terraingen = tgc.getConstructor(ConfigurationNode.class).newInstance(node);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// World population
		populate=cfg.getBoolean("populate", true);
		for(Object o : cfg.getList("populators")) {
			if(o instanceof ConfigurationNode) {
				String populatorName = ((ConfigurationNode) o).getString("name");
				Class<? extends SpadeEffectGenerator> c;
				try {
					c = (Class<? extends SpadeEffectGenerator>) Class.forName(populatorName);
					SpadeEffectGenerator seg = c.getConstructor(ConfigurationNode.class).newInstance(o);
					populators.add(seg);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public void generate(World world, int X, int Z, byte[][][] blocks, Biome[][] biomes, double[][] temperature) {
		terraingen.generateChunk(world, X, Z, blocks, biomes, temperature);
	}
	
    public void populate(World w, int X, int Z)
    {
    	Chunk c = w.getChunkAt(X, Z);
        for(SpadeEffectGenerator g:populators) {
        	g.addToChunk(c, X, Z);
        }
    }

}
