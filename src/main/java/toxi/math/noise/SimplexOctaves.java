package toxi.math.noise;

public class SimplexOctaves {
    private final SimplexNoise noisegen;
    public int octaves;
    
    public SimplexOctaves(final long seed, final int octaves) {
        noisegen = new SimplexNoise(seed);
        this.octaves = octaves;
    }
    
    public double noise(final double x, final double y, final double z) {
        double v = 0d;
        double d = 1d;
        for (int i = 0; i < octaves; i++) {
            v += noisegen.noise(x * d, y * d, z * d) / d;
            d /= 2D;
        }
        return v;
    }
    
}
