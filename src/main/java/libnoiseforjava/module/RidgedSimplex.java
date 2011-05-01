
package libnoiseforjava.module;

import toxi.math.noise.SimplexNoise;

public class RidgedSimplex
{
	private double m_amplitude;
	private double m_frequency;
	public RidgedSimplex(long seed)
	{
		m_simplex = new SimplexNoise(seed);
	}
	
	public double sample(double x, double y)
	{
		double noise = m_simplex.noise(x * m_frequency, y * m_frequency);
		
		if (noise < 0)
			noise = -noise;
			
		noise = 1.0 - noise;		
		return noise * m_amplitude;
	}
	
	// Returns noise in the interval of [0, 1].	
	public double sample(double x, double y, double z)
	{		
		double noise = m_simplex.noise(x * m_frequency, y * m_frequency, z * m_frequency);
		
		if (noise < 0)
			noise = -noise;

		noise = 1.0 - noise;
		return noise * m_amplitude;
	}
	
	private SimplexNoise m_simplex;
	public void setFrequency(double d) {
		m_frequency=d;
	}

	public void setAmplitude(int i) {
		m_amplitude=i;
	}
}
