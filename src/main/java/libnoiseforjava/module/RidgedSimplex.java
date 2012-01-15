package libnoiseforjava.module;

import toxi.math.noise.SimplexNoise;

public class RidgedSimplex {
    private double m_amplitude;
    private double m_frequency;
    
    public RidgedSimplex(final long seed) {
        m_simplex = new SimplexNoise(seed);
    }
    
    public double sample(final double x, final double y) {
        double noise = m_simplex.noise(x * m_frequency, y * m_frequency);
        
        if (noise < 0) {
            noise = -noise;
        }
        
        noise = 1.0 - noise;
        return noise * m_amplitude;
    }
    
    // Returns noise in the interval of [0, 1].	
    public double sample(final double x, final double y, final double z) {
        double noise = m_simplex.noise(x * m_frequency, y * m_frequency, z * m_frequency);
        
        if (noise < 0) {
            noise = -noise;
        }
        
        noise = 1.0 - noise;
        return noise * m_amplitude;
    }
    
    private final SimplexNoise m_simplex;
    
    public void setFrequency(final double d) {
        m_frequency = d;
    }
    
    public void setAmplitude(final int i) {
        m_amplitude = i;
    }
}
