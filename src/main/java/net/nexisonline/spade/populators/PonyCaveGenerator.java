package net.nexisonline.spade.populators;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import libnoiseforjava.module.RidgedSimplex;
import net.minecraft.server.Block;
import net.nexisonline.spade.InterpolatedDensityMap;
import net.nexisonline.spade.SpadeLogging;
import net.nexisonline.spade.SpadePlugin;

import org.bukkit.Chunk;
import org.bukkit.World;

import toxi.math.noise.SimplexNoise;

public class PonyCaveGenerator extends SpadeEffectGenerator {
    
    InterpolatedDensityMap m_interpolator;
    
    RidgedSimplex m_ridged1;
    RidgedSimplex m_ridged2;
    
    SimplexNoise m_xTurbulence;
    SimplexNoise m_yTurbulence;
    SimplexNoise m_zTurbulence;
    
    SimplexNoise m_simplex1;
    SimplexNoise m_simplex2;
    SimplexNoise m_simplex3;
    SimplexNoise m_simplex4;
    
    SimplexNoise m_simplex5;
    SimplexNoise m_simplex6;
    
    public PonyCaveGenerator(final SpadePlugin plugin, final Map<String, Object> node, final long seed) {
        super(plugin, node, seed);
        
        // Configure
        
        // Setup
        m_interpolator = new InterpolatedDensityMap();
        
        m_xTurbulence = new SimplexNoise(seed + 1);
        m_xTurbulence.setFrequency(0.05);
        m_xTurbulence.setAmplitude(10);
        
        m_yTurbulence = new SimplexNoise(seed + 2);
        m_yTurbulence.setFrequency(0.05);
        m_yTurbulence.setAmplitude(10);
        
        m_zTurbulence = new SimplexNoise(seed + 3);
        m_zTurbulence.setFrequency(0.05);
        m_zTurbulence.setAmplitude(10);
        
        m_ridged1 = new RidgedSimplex(seed + 7);
        m_ridged1.setFrequency(0.01);
        m_ridged1.setAmplitude(9);
        
        m_ridged2 = new RidgedSimplex(seed + 8);
        m_ridged2.setFrequency(0.01);
        m_ridged2.setAmplitude(11);
    }
    
    public static SpadeEffectGenerator getInstance(final SpadePlugin plugin, final Map<String, Object> n, final long seed) {
        final Map<String, Object> node = n;
        return new PonyCaveGenerator(plugin, node, seed);
    }
    
    @Override
    public void populate(final World world, final Random random, final Chunk chunk) {
        final int X = chunk.getX();
        final int Z = chunk.getZ();
        SpadeLogging.info(String.format("Generating caves in chunk (%d,%d)", chunk.getX(), chunk.getZ()));
        double density = 0;
        
        for (int x = 0; x < 16; x += 5) {
            for (int z = 0; z < 16; z += 5) {
                for (int y = 0; y < 128; y += 16) {
                    final double posX = x + (X * 16);
                    final double posY = y - 64;
                    final double posZ = z + (Z * 16);
                    
                    final double warpX = posX + m_xTurbulence.sample(posX, posY, posZ);
                    final double warpY = posY + m_yTurbulence.sample(posX, posY, posZ);
                    final double warpZ = posZ + m_zTurbulence.sample(posX, posY, posZ);
                    
                    density = -12;
                    
                    density += m_ridged1.sample(warpX, warpY, warpZ);
                    density += m_ridged2.sample(warpX, warpY, warpZ);
                    
                    //density -= (y * 0.5);
                    
                    m_interpolator.setDensity(x, y, z, density);
                }
            }
        }
        
        m_interpolator.interpolate();
        
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 126; y > 2; y--) {
                    final byte id = (byte) chunk.getBlock(x, y, z).getTypeId();
                    
                    if ((m_interpolator.getDensity(x, y, z) > 5) && !blockIsWater(chunk, x, y, z) && !((id == Block.LAVA.id) || (id == Block.STATIONARY_LAVA.id) || (id == Block.BEDROCK.id)) && !blockIsWater(chunk, x + 1, y, z) && !blockIsWater(chunk, x - 1, y, z) && !blockIsWater(chunk, x, y + 1, z) && !blockIsWater(chunk, x, y - 1, z) && !blockIsWater(chunk, x, y, z + 1) && !blockIsWater(chunk, x, y, z - 1)) {
                        try {
                            chunk.getBlock(x, y, z).setTypeId(((y < 10) ? Block.STATIONARY_LAVA.id : 0));
                        } catch (final Exception e) {
                            
                        }
                    }
                }
            }
        }
    }
    
    private boolean blockIsWater(final Chunk chunk, final int x, final int y, final int z) {
        if ((x < 0) || (x > 15) || (z < 0) || (z > 15) || (y < 0) || (y > 127))
            return false;
        final byte id = (byte) chunk.getBlock(x, y, z).getTypeId();
        return (id == Block.WATER.id) || (id == Block.STATIONARY_WATER.id);
    }
    
    @Override
    public Map<String, Object> getConfiguration() {
        final Map<String, Object> n = new HashMap<String, Object>();
        return n;
    }
}
