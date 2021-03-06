/**
 * Pony's Handy Dandy Rape Generator
 *
 * INSERT BSD HERE
 */
package net.nexisonline.spade.chunkproviders;

import java.util.Map;
import java.util.Random;

import net.nexisonline.spade.SpadeChunkProvider;
import net.nexisonline.spade.SpadeLogging;
import net.nexisonline.spade.SpadePlugin;

import org.bukkit.Material;
import org.bukkit.World;

import toxi.math.noise.SimplexOctaves;

/**
 * @author PrettyPonyyy
 * 
 */
public class ChunkProviderMountains2 extends SpadeChunkProvider {
    private static final int WATER_HEIGHT = 32;
    net.minecraft.server.World p = null;
    
    private SpadePlugin plugin;
    private SimplexOctaves m_simplexGenerator;
    private SimplexOctaves m_simplexGenerator2;
    
    public ChunkProviderMountains2(final SpadePlugin plugin) {
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
            m_simplexGenerator = new SimplexOctaves(1234, 4);
            m_simplexGenerator2 = new SimplexOctaves(1234 + 51, 4);
        } catch (final Exception e) {
        }
    }
    
    private static double lerp(final double a, final double b, final double f) {
        return (a + ((b - a) * f));
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.bukkit.ChunkProvider#generateChunk(int, int, byte[], org.bukkit.block.Biome[], double[])
     */
    @Override
    public byte[] generate(final World world, final Random random, final int X, final int Z) {
        final byte[] blocks = new byte[16 * 16 * 128];
        if (!plugin.shouldGenerateChunk(worldName, X, Z)) {
            SpadeLogging.debug(String.format("[DoublePerlin] SKIPPING Chunk (%d,%d)", X, Z));
            return blocks;
        }
        
        final double density[][][] = new double[16][128][16];
        
        for (int x = 0; x < 16; x += 3) {
            for (int y = 0; y < 128; y += 3) {
                for (int z = 0; z < 16; z += 3) {
                    final double posX = (x + (X * 16));
                    final double posY = (y - 96);
                    final double posZ = (z + (Z * 16));
                    
                    final double warp = 0.004;
                    final double warpMod = m_simplexGenerator.noise(posX * warp, posY * warp, posZ * warp) * 5;//m_fractalGenerator.getValue(posX * warp, posY * warp, posZ * warp) * 5;
                    final double warpPosX = posX * warpMod;
                    final double warpPosY = posY * warpMod;
                    final double warpPosZ = posZ * warpMod;
                    
                    final double mod = m_simplexGenerator2.noise(warpPosX * 0.0005, warpPosY * 0.0005, warpPosZ * 0.005);//m_perlinGenerator.getValue(warpPosX * 0.0005, warpPosY * 0.0005, warpPosZ * 0.005);
                    
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
        
        //density=Interpolator.LinearExpandDensitymap(density, 16, 128, 16);
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 128; y++) {
                for (int z = 0; z < 16; z++) {
                    byte block = 0;
                    if ((int) density[x][y][z] > 5) {
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
        //SpadeLogging.debug(String.format("[DoublePerlin %d] Chunk (%d,%d)",m_perlinGenerator.getSeed(),X,Z));
        return blocks;
    }
    
}
