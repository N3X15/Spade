/**
 * Pony's Handy Dandy Rape Generator 2.0
 *
 * INSERT BSD HERE
 */
package net.nexisonline.spade.chunkproviders;

import java.util.Map;
import java.util.Random;

import net.nexisonline.spade.InterpolatedDensityMap;
import net.nexisonline.spade.SpadeChunkProvider;
import net.nexisonline.spade.SpadeLogging;
import net.nexisonline.spade.SpadePlugin;

import org.bukkit.Material;
import org.bukkit.World;

import toxi.math.noise.SimplexNoise;

/**
 * 
 * @author PrettyPonyyy
 * 
 * 
 */
public class ChunkProviderSurrealIslands extends SpadeChunkProvider {
    private int WATER_HEIGHT = 32;
    private int OCEAN_FLOOR = 16;
    SimplexNoise m_simplex1;
    SimplexNoise m_simplex2;
    SimplexNoise m_xTurbulence;
    SimplexNoise m_yTurbulence;
    SimplexNoise m_zTurbulence;
    private SimplexNoise m_SeaFloorNoise;
    private final InterpolatedDensityMap density;
    
    public ChunkProviderSurrealIslands(final SpadePlugin plugin) {
        super(plugin);
        density = new InterpolatedDensityMap();
    }
    
    @Override
    public void onLoad(final String worldName, final long worldSeed, final Map<String, Object> map) {
        super.onLoad(worldName, worldSeed, map);
        
        /** Configure **/
        WATER_HEIGHT = (Integer) map.get("water-level");
        OCEAN_FLOOR = (Integer) map.get("min-ocean-floor-height");
        
        try {
            m_simplex1 = new SimplexNoise(((int) worldSeed * 1024));
            m_simplex1.setFrequency(0.005);
            m_simplex1.setAmplitude(50);
            
            m_simplex2 = new SimplexNoise(((int) worldSeed * 1024) + 1);
            m_simplex2.setFrequency(0.0005);
            m_simplex2.setAmplitude(25);
            
            m_xTurbulence = new SimplexNoise(((int) worldSeed * 1024) + 2);
            m_xTurbulence.setFrequency(0.05);
            m_xTurbulence.setAmplitude(5);
            
            m_yTurbulence = new SimplexNoise(((int) worldSeed * 1024) + 3);
            m_yTurbulence.setFrequency(0.05);
            m_yTurbulence.setAmplitude(5);
            
            m_zTurbulence = new SimplexNoise(((int) worldSeed * 1024) + 4);
            m_zTurbulence.setFrequency(0.05);
            m_zTurbulence.setAmplitude(5);
            
            m_SeaFloorNoise = new SimplexNoise(((int) worldSeed * 1024) + 4);
            
        } catch (final Exception e) {
        }
    }
    
    @Override
    public byte[] generate(final World world, final Random rand, final int X, final int Z) {
        final byte[] blocks = new byte[16 * 128 * 16];
        double frequency = 0;
        double amplitude = 0;
        for (int x = 0; x < 16; x += 5) {
            for (int y = 0; y <= 128; y += 16) {
                for (int z = 0; z < 16; z += 5) {
                    final double posX = (x + (X * 16));
                    final double posY = (y - 64);
                    final double posZ = (z + (Z * 16));
                    frequency = 0.05;
                    amplitude = 10;
                    final double warpX = posX + m_xTurbulence.sample(posX, posY, posZ, frequency, amplitude);
                    final double warpY = posY + m_yTurbulence.sample(posX, posY, posZ, frequency, amplitude);
                    final double warpZ = posZ + m_zTurbulence.sample(posX, posY, posZ, frequency, amplitude);
                    double d = -12;
                    frequency = 0.005;
                    amplitude = 50;
                    d -= m_simplex1.sample(warpX, warpY, warpZ, frequency, amplitude);
                    frequency = 0.0005;
                    amplitude = 25;
                    d -= m_simplex2.sample(warpX, warpY, warpZ, frequency, amplitude);
                    density.setDensity(x, y, z, d);
                }
            }
        }
        density.interpolate();
        double minDensity = Double.MAX_VALUE;
        double maxDensity = Double.MIN_VALUE;
        for (int x = 0; x < 16; ++x) {
            for (int y = 0; y < 128; ++y) {
                for (int z = 0; z < 16; ++z) {
                    byte block = 0;
                    final double d = density.getDensity(x, y, z);
                    maxDensity = Math.max(maxDensity, d);
                    minDensity = Math.min(minDensity, d);
                    if ((int) d > 5) {
                        block = 1;
                    } else {
                        block = (byte) ((y < WATER_HEIGHT) ? Material.STATIONARY_WATER.getId() : 0);
                    }
                    if ((y <= (OCEAN_FLOOR + m_SeaFloorNoise.sample((x + (X * 16)), (z + (Z * 16)), 0.01, 5))) && ((block == Material.STATIONARY_WATER.getId()) || (block == Material.WATER.getId()))) {
                        block = 1;
                    }
                    if (y == 1) {
                        block = 7;
                    }
                    setBlockByte(blocks, x, y, z, block);
                }
            }
        }
        SpadeLogging.debug(String.format("[Islands] Chunk (%d,%d) (densityRange= [%.2f,%.2f])", X, Z, minDensity, maxDensity));
        return blocks;
    }
    
    @Override
    public Map<String, Object> getConfig() {
        final Map<String, Object> cfg = super.getConfig();
        cfg.put("water-level", WATER_HEIGHT);
        cfg.put("min-ocean-floor-height", OCEAN_FLOOR);
        return cfg;
    }
}
