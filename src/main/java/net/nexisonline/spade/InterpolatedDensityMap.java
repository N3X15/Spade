package net.nexisonline.spade;

public class InterpolatedDensityMap
{
	private double[][][] m_density;
	
	public InterpolatedDensityMap()
	{
		m_density = new double[16][128][16];
	}
	
	@Deprecated
	public void interpolateLowRes()
	{		
		for (int x = 0; x < 16; x += 5)
		{
			for (int y = 0; y < 128; y++)
			{
				for (int z = 0; z < 16; z += 5)
				{
					if (x == 0 && z > 0)
					{
						m_density[x][y][z - 1] = lerp(m_density[x][y][z], m_density[x][y][z - 5], 0.2);
						m_density[x][y][z - 2] = lerp(m_density[x][y][z], m_density[x][y][z - 5], 0.4);
						m_density[x][y][z - 3] = lerp(m_density[x][y][z], m_density[x][y][z - 5], 0.6);
						m_density[x][y][z - 4] = lerp(m_density[x][y][z], m_density[x][y][z - 5], 0.8);
					}
					else if (x > 0 && z > 0)
					{
						m_density[x - 1][y][z] = lerp(m_density[x][y][z], m_density[x - 5][y][z], 0.2);
						m_density[x - 2][y][z] = lerp(m_density[x][y][z], m_density[x - 5][y][z], 0.4);
						m_density[x - 3][y][z] = lerp(m_density[x][y][z], m_density[x - 5][y][z], 0.6);
						m_density[x - 4][y][z] = lerp(m_density[x][y][z], m_density[x - 5][y][z], 0.8);
					
						m_density[x][y][z - 1] = lerp(m_density[x][y][z], m_density[x][y][z - 5], 0.2);
						m_density[x][y][z - 2] = lerp(m_density[x][y][z], m_density[x][y][z - 5], 0.4);
						m_density[x][y][z - 3] = lerp(m_density[x][y][z], m_density[x][y][z - 5], 0.6);
						m_density[x][y][z - 4] = lerp(m_density[x][y][z], m_density[x][y][z - 5], 0.8);
						
						for (int i = 1; i <= 4; i++)
						{
							for (int j = 1; j <= 4; j++)
							{
								m_density[x - j][y][z - i] = lerp(m_density[x][y][z], m_density[x - 5][y][z - 5], 0.2 * j);
							}
						}
					}
					else if (x > 0 && z == 0)
					{
						m_density[x - 1][y][z] = lerp(m_density[x][y][z], m_density[x - 5][y][z], 0.2);
						m_density[x - 2][y][z] = lerp(m_density[x][y][z], m_density[x - 5][y][z], 0.4);
						m_density[x - 3][y][z] = lerp(m_density[x][y][z], m_density[x - 5][y][z], 0.6);
						m_density[x - 4][y][z] = lerp(m_density[x][y][z], m_density[x - 5][y][z], 0.8);
					}
				}
			}
		}
	}
	
	private double it(int iter)
	{
		return (double)iter * (1.0 / 16.0);
	}

	public void setDensity(int x, int y, int z, double value)
	{
		try
		{
			m_density[x][y][z] = value;
			
			if (y > 0)
			{
				m_density[x][y - 1][z] = lerp(m_density[x][y][z], m_density[x][y - 16][z], it(1));
				m_density[x][y - 2][z] = lerp(m_density[x][y][z], m_density[x][y - 16][z], it(2));
				m_density[x][y - 3][z] = lerp(m_density[x][y][z], m_density[x][y - 16][z], it(3));
				m_density[x][y - 4][z] = lerp(m_density[x][y][z], m_density[x][y - 16][z], it(4));				
				m_density[x][y - 5][z] = lerp(m_density[x][y][z], m_density[x][y - 16][z], it(5));
				m_density[x][y - 6][z] = lerp(m_density[x][y][z], m_density[x][y - 16][z], it(6));
				m_density[x][y - 7][z] = lerp(m_density[x][y][z], m_density[x][y - 16][z], it(7));
				m_density[x][y - 8][z] = lerp(m_density[x][y][z], m_density[x][y - 16][z], it(8));
				m_density[x][y - 9][z] = lerp(m_density[x][y][z], m_density[x][y - 16][z], it(9));
				m_density[x][y - 10][z] = lerp(m_density[x][y][z], m_density[x][y - 16][z], it(10));
				m_density[x][y - 11][z] = lerp(m_density[x][y][z], m_density[x][y - 16][z], it(11));
				m_density[x][y - 12][z] = lerp(m_density[x][y][z], m_density[x][y - 16][z], it(12));
				m_density[x][y - 13][z] = lerp(m_density[x][y][z], m_density[x][y - 16][z], it(13));
				m_density[x][y - 14][z] = lerp(m_density[x][y][z], m_density[x][y - 16][z], it(14));
				m_density[x][y - 15][z] = lerp(m_density[x][y][z], m_density[x][y - 16][z], it(15));				
			}
		}
		catch (Exception e)
		{
			System.out.println("Error setting density. You probably didn't initialize the interpolator.");
		}
	}
	
	public double getDensity(int x, int y, int z)
	{
		return m_density[x][y][z];
	}
	
	// Linear interpolation
	private double lerp(double a, double b, double f)
	{
		return (a + (b - a) * f);
	}
}
