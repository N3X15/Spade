/**
 * Pony's Handy Dandy Rape Generator
 *
 * INSERT BSD HERE
 */
package net.nexisonline.spade.chunkproviders;

import java.util.Map;
import java.util.Random;

import libnoiseforjava.module.Perlin;
import libnoiseforjava.module.RidgedMulti;
import net.nexisonline.spade.SpadeChunkProvider;
import net.nexisonline.spade.SpadePlugin;

import org.bukkit.Material;
import org.bukkit.World;

/**
 * @author PrettyPonyyy
 * 
 */
public class ChunkProviderWat extends SpadeChunkProvider {
    public int distanceSquared = -1;
    private static final int WATER_HEIGHT = 32;
    
    private Perlin m_perlinGenerator;
    private RidgedMulti m_fractalGenerator;
    private SpadePlugin plugin;
    
    public ChunkProviderWat(final SpadePlugin plugin) {
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
        try {
            m_perlinGenerator = new Perlin(); //new Perlin();
            m_fractalGenerator = new RidgedMulti(); //new Perlin();
            
            m_perlinGenerator.setSeed((int) (worldSeed * 1024));
            m_perlinGenerator.setOctaveCount(1);
            m_perlinGenerator.setFrequency(1f);
            
            m_fractalGenerator.setSeed((int) (worldSeed * 1024));
            m_fractalGenerator.setOctaveCount(1);
            m_fractalGenerator.setFrequency(0.25f);
            
        } catch (final Exception e) {
        }
    }
    
    private static double lerp(final double a, final double b, final double f) {
        return ((a * (1 - f)) + (b * f));
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.bukkit.ChunkProvider#generateChunk(int, int, byte[], org.bukkit.block.Biome[], double[])
     */
    @Override
    public byte[] generate(final World world, final Random random, final int X, final int Z) {
        final byte[] blocks = new byte[16 * 16 * 128];
        if (!plugin.shouldGenerateChunk(worldName, X, Z))
            return blocks;
        
        final double density[][][] = new double[16][128][16];
        
        for (int x = 0; x < 16; x += 3) {
            for (int y = 0; y < 128; y += 3) {
                for (int z = 0; z < 16; z += 3) {
                    final double posX = x + (X * 16);
                    final double posY = y - 96;
                    final double posZ = z + (Z * 16);
                    
                    final double absPosX = Math.abs(posX);
                    final double absPosY = Math.abs(posY);
                    final double absPosZ = Math.abs(posZ);
                    
                    final double warp = 0.004;
                    final double warpMod = m_fractalGenerator.getValue(posX * warp, posY * warp, posZ * warp) * 5;
                    final double warpPosX = absPosX * warpMod;
                    final double warpPosY = absPosY * warpMod;
                    final double warpPosZ = absPosZ * warpMod;
                    
                    final double mod = m_perlinGenerator.getValue(warpPosX * 0.005, warpPosY * 0.005, warpPosZ * 0.005);
                    
                    density[x][y][z] = -(y - 64);
                    density[x][y][z] += mod * 100;
                }
            }
        }
        
        for (int x = 0; x < 16; x += 3) {
            for (int y = 0; y < 128; y += 3) {
                for (int z = 0; z < 16; z += 3) {
                    if (y != 126) {
                        density[x][y + 1][z] = lerp(density[x][y][z], density[x][y + 3][z], 0.2);
                        density[x][y + 2][z] = lerp(density[x][y][z], density[x][y + 3][z], 0.8);
                    }
                }
            }
        }
        
        for (int x = 0; x < 16; x += 3) {
            for (int y = 0; y < 128; y++) {
                for (int z = 0; z < 16; z += 3) {
                    if ((x == 0) && (z > 0)) {
                        density[x][y][z - 1] = lerp(density[x][y][z], density[x][y][z - 3], 0.25);
                        density[x][y][z - 2] = lerp(density[x][y][z], density[x][y][z - 3], 0.85);
                    } else if ((x > 0) && (z > 0)) {
                        density[x - 1][y][z] = lerp(density[x][y][z], density[x - 3][y][z], 0.25);
                        density[x - 2][y][z] = lerp(density[x][y][z], density[x - 3][y][z], 0.85);
                        
                        density[x][y][z - 1] = lerp(density[x][y][z], density[x][y][z - 3], 0.25);
                        density[x - 1][y][z - 1] = lerp(density[x][y][z], density[x - 3][y][z - 3], 0.25);
                        density[x - 2][y][z - 1] = lerp(density[x][y][z], density[x - 3][y][z - 3], 0.85);
                        
                        density[x][y][z - 2] = lerp(density[x][y][z], density[x][y][z - 3], 0.25);
                        density[x - 1][y][z - 2] = lerp(density[x][y][z], density[x - 3][y][z - 3], 0.85);
                        density[x - 2][y][z - 2] = lerp(density[x][y][z], density[x - 3][y][z - 3], 0.85);
                    } else if ((x > 0) && (z == 0)) {
                        density[x - 1][y][z] = lerp(density[x][y][z], density[x - 3][y][z], 0.25);
                        density[x - 2][y][z] = lerp(density[x][y][z], density[x - 3][y][z], 0.85);
                    }
                }
            }
        }
        
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 128; y++) {
                for (int z = 0; z < 16; z++) {
                    byte block = 0;
                    if (density[x][y][z] > 0) {
                        block = 1;
                    } else {
                        block = (byte) ((y < WATER_HEIGHT) ? Material.STATIONARY_WATER.getId() : 0);
                    }
                    // Origin point + sand to prevent 5000 years of loading.
                    if ((x == 0) && (z == 0) && (X == x) && (Z == z) && (y <= 63)) {
                        block = (byte) ((y == 125) ? 12 : 7);
                    }
                    if (y == 1) {
                        block = 7;
                    }
                    setBlockByte(blocks, x, y, z, block);
                }
            }
        }
        
        //Logger.getLogger("Minecraft").info(String.format("[wat] Chunk (%d,%d)",X,Z));
        return blocks;
    }
}
