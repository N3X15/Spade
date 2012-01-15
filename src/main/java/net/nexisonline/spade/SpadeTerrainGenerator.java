package net.nexisonline.spade;

import java.util.Map;

import org.bukkit.World;
import org.bukkit.block.Biome;

public abstract class SpadeTerrainGenerator {
    public SpadeTerrainGenerator(final Map<String, Object> cfg) {
    }
    
    public abstract void generateChunk(World world, int X, int Z, byte[][][] blocks, Biome[][] biomes, double[][] temperature);
}
