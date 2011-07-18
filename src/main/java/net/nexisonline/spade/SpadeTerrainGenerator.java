package net.nexisonline.spade;

import java.util.Map;

import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.util.config.ConfigurationNode;

public abstract class SpadeTerrainGenerator {
	public SpadeTerrainGenerator(Map<String,Object> cfg) {}
	public abstract void generateChunk(World world, int X, int Z, byte[][][] blocks, Biome[][] biomes, double[][] temperature);
}
