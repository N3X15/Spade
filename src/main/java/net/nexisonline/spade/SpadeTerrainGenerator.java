package net.nexisonline.spade;

import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.util.config.ConfigurationNode;

public abstract class SpadeTerrainGenerator {
	public SpadeTerrainGenerator(ConfigurationNode cfg) {}
	public abstract void generateChunk(World world, int X, int Z, byte[][][] blocks, Biome[][] biomes, double[][] temperature);
}
