package net.nexisonline.spade.generators;

import libnoiseforjava.module.RidgedSimplex;
import net.minecraft.server.Block;
import net.nexisonline.spade.InterpolatedDensityMap;
import toxi.math.noise.SimplexNoise;

public class PonyCaveGenerator
{

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

	public PonyCaveGenerator(long seed) {
		m_interpolator = new InterpolatedDensityMap();

		/*
		m_simplex1 = new SimplexNoise(seed + 1);
		m_simplex1.setFrequency(0.05);
		m_simplex1.setAmplitude(5);

		m_simplex2 = new SimplexNoise(seed + 2);
		m_simplex2.setFrequency(0.005);
		m_simplex2.setAmplitude(15);

		m_simplex3 = new SimplexNoise(seed + 3);
		m_simplex3.setFrequency(0.0005);
		m_simplex3.setAmplitude(20);

		m_simplex4 = new SimplexNoise(seed + 4);
		m_simplex4.setFrequency(0.00005);
		m_simplex4.setAmplitude(25);

		m_simplex5 = new SimplexNoise(seed + 5);
		m_simplex5.setFrequency(0.1);
		m_simplex5.setAmplitude(50);

		m_simplex6 = new SimplexNoise(seed + 6);
		m_simplex6.setFrequency(0.1);
		m_simplex6.setAmplitude(25);
		 */

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

	public void generateCaves(Object world, int X, int Z, byte[] data)
	{
		double density = 0;

		for (int x = 0; x < 16; x+=5)
		{
			for (int z = 0; z < 16; z+=5)
			{
				for (int y = 0; y < 128; y += 16)
				{
					double posX = x + (X * 16);
					double posY = y - 64;
					double posZ = z + (Z * 16);

					double warpX = posX + m_xTurbulence.sample(posX, posY, posZ);
					double warpY = posY + m_yTurbulence.sample(posX, posY, posZ);
					double warpZ = posZ + m_zTurbulence.sample(posX, posY, posZ);

					density = -12;

					density += m_ridged1.sample(warpX, warpY, warpZ);					
					density += m_ridged2.sample(warpX, warpY, warpZ);

					//density -= (y * 0.5);

					m_interpolator.setDensity(x, y, z, density);
				}
			}
		}

		m_interpolator.interpolate();

		for (int x = 0; x < 16; x++)
		{
			for (int z = 0; z < 16; z++)
			{
				for (int y = 126; y > 2; y--)
				{
					byte id = data[x << 11 | z << 7 | y];

					if (m_interpolator.getDensity(x, y, z) > 5 &&
							!blockIsWater(data,x,y,z) &&
							!(
									id==Block.LAVA.id ||
									id==Block.STATIONARY_LAVA.id ||
									id==Block.BEDROCK.id
							) && 
							!blockIsWater(data,x+1,y,z) && 
							!blockIsWater(data,x-1,y,z) && 
							!blockIsWater(data,x,y+1,z) && 
							!blockIsWater(data,x,y-1,z) &&
							!blockIsWater(data,x,y,z+1) && 
							!blockIsWater(data,x,y,z-1)
					)
					{
						data[x << 11 | z << 7 | y]=(byte) ((y<10)?Block.STATIONARY_LAVA.id:0);
					}
				}
			}
		}
	}

	private boolean blockIsWater(byte[] data,int x, int y, int z) {
		if(x<0||x>15||z<0||z>15||y<0||y>127) return false;
		byte id = data[x << 11 | z << 7 | y];
		return	id==Block.WATER.id ||
		id==Block.STATIONARY_WATER.id;
	}
}

