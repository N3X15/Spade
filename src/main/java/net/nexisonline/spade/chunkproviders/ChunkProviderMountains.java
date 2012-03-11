/**
 * N3X15's Handy Dandy Mountain Generator
 * 	From MineEdit
 * 
 * INSERT BSD HERE
 */
package net.nexisonline.spade.chunkproviders;

import java.util.Map;
import java.util.Random;

import libnoiseforjava.NoiseGen.NoiseQuality;
import libnoiseforjava.module.Perlin;
import libnoiseforjava.module.RidgedMulti;
import net.nexisonline.spade.SpadeChunkProvider;
import net.nexisonline.spade.SpadeLogging;
import net.nexisonline.spade.SpadePlugin;

import org.bukkit.Material;
import org.bukkit.World;

/**
 * @author N3X15
 * 
 */
public class ChunkProviderMountains extends SpadeChunkProvider {
    private RidgedMulti terrainNoise;
    private Perlin continentNoise;
    private int continentNoiseOctaves = 16;
    private final NoiseQuality noiseQuality = NoiseQuality.QUALITY_STD;
    
    public ChunkProviderMountains(final SpadePlugin plugin) {
        super(plugin);
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.bukkit.ChunkProvider#onLoad(org.bukkit.World, long)
     */
    @Override
    public void onLoad(final String worldName, final long worldSeed, final Map<String, Object> map) {
        super.onLoad(worldName, worldSeed, map);
        
        final double Frequency = 0.1;
        final double Lacunarity = 0.05;
        final int OctaveCount = continentNoiseOctaves = 4;
        
        try {
            terrainNoise = new RidgedMulti();
            continentNoise = new Perlin();
            terrainNoise.setSeed((int) worldSeed);
            continentNoise.setSeed((int) worldSeed + 2);
            
            terrainNoise.setFrequency(Frequency);
            terrainNoise.setNoiseQuality(noiseQuality);
            terrainNoise.setOctaveCount(OctaveCount);
            terrainNoise.setLacunarity(Lacunarity);
            
            // continentNoise.setFrequency(ContinentNoiseFrequency);
            // continentNoise.setNoiseQuality(noiseQuality);
            continentNoise.setOctaveCount(continentNoiseOctaves);
            // continentNoise.setLacunarity(Lacunarity);
            // continentNoise.setPersistence(Persistance);
        } catch (final Exception e) {
        }
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.bukkit.ChunkProvider#generateChunk(int, int, byte[], org.bukkit.block.Biome[], double[])
     */
    @Override
    public byte[] generate(final World world, final Random random, final int X, final int Z) {
        
        final byte[] blocks = new byte[16 * 16 * 128];
        int minHeight = Integer.MAX_VALUE;
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                // Generate our continental noise.
                final int height = (int) ((continentNoise.getValue((x + (X * 16)) * 0.01, (z + (Z * 16)) * 0.01, 0) * 32d) + 80d);
                if (height < minHeight) {
                    minHeight = height;
                }
                for (int y = 0; y < 128; y++) {
                    
                    byte block = (y <= height) ? (byte) 1 : (byte) 0; // Fill;
                    block = (block > 0) ? (byte) 1 : (byte) 0;
                    // If below height, set rock. Otherwise, set air.
                    block = ((y <= 63) && (block == 0)) ? (byte) 9 : block; // Water
                    
                    setBlockByte(blocks, x, y, z, (byte) ((y < 2) ? Material.BEDROCK.getId() : block));
                }
            }
        }
        SpadeLogging.debug(String.format("[Mountains] Chunk (%d,%d) Min Height: %dm", X, Z, minHeight));
        return blocks;
    }
}
