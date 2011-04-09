package toxi.math.noise;

public class SimplexOctaves {
	private SimplexNoise noisegen;
	public int octaves;
	public SimplexOctaves(long seed, int octaves) {
		noisegen=new SimplexNoise(seed);
		this.octaves=octaves;
	}
	
	public double noise(double x, double y, double z) {
		double v = 0d;
		double d = 1d;
		for(int i = 0; i < octaves; i++)
		{
			v += noisegen.noise(x*d, y*d, z*d) / d;
			d /= 2D;
		}
		return v;
	}

}
