/**
 * Pony's Handy Dandy Rape Generator
 *
 * INSERT BSD HERE
 */
package net.nexisonline.spade.chunkproviders;

import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import net.nexisonline.spade.InterpolatedDensityMap;
import net.nexisonline.spade.SpadeChunkProvider;
import net.nexisonline.spade.SpadePlugin;

import org.bukkit.Material;
import org.bukkit.World;

import toxi.math.noise.SimplexOctaves;

/**
 * @author PrettyPonyyy
 * 
 */
public class ChunkProviderDoublePerlin extends SpadeChunkProvider {
    private static final int WATER_HEIGHT = 32;
    net.minecraft.server.World p = null;
    
    private SpadePlugin plugin;
    private SimplexOctaves m_simplexGenerator;
    private SimplexOctaves m_simplexGenerator2;
    
    public ChunkProviderDoublePerlin(final SpadePlugin plugin) {
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
    
    @Override
    public byte[] generate(final World world, final Random random, final int X, final int Z) {
        final byte[] blocks = new byte[16 * 16 * 128];
        if (!plugin.shouldGenerateChunk(worldName, X, Z)) {
            Logger.getLogger("Minecraft").info(String.format("[DoublePerlin] SKIPPING Chunk (%d,%d)", X, Z));
            return blocks;
        }
        
        final InterpolatedDensityMap density = new InterpolatedDensityMap();
        
        for (int x = 0; x < 16; ++x) {
            for (int y = 0; y < 128; y += 16) {
                for (int z = 0; z < 16; ++z) {
                    final double posX = (x + (X * 16));
                    final double posY = (y - 96);
                    final double posZ = (z + (Z * 16));
                    
                    final double warp = 0.004;
                    final double warpMod = m_simplexGenerator.noise(posX * warp, posY * warp, posZ * warp) * 5;//m_fractalGenerator.getValue(posX * warp, posY * warp, posZ * warp) * 5;
                    final double warpPosX = posX * warpMod;
                    final double warpPosY = posY * warpMod;
                    final double warpPosZ = posZ * warpMod;
                    
                    final double mod = m_simplexGenerator2.noise(warpPosX * 0.0005, warpPosY * 0.0005, warpPosZ * 0.005);//m_perlinGenerator.getValue(warpPosX * 0.0005, warpPosY * 0.0005, warpPosZ * 0.005);
                    
                    density.setDensity(x, y, z, (-(y - 64)) + (mod * 100));
                }
            }
        }
        density.interpolate();
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 128; y++) {
                for (int z = 0; z < 16; z++) {
                    byte block = 0;
                    if ((int) density.getDensity(x, y, z) > 5) {
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
        //Logger.getLogger("Minecraft").info(String.format("[DoublePerlin %d] Chunk (%d,%d)",m_perlinGenerator.getSeed(),X,Z));
        return blocks;
    }
}
