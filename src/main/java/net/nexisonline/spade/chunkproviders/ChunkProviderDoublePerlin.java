/**
 * Pony's Handy Dandy Rape Generator
 *
 * INSERT BSD HERE
 */
package net.nexisonline.spade.chunkproviders;

import java.util.logging.Logger;

import libnoiseforjava.module.Perlin;
import org.bukkit.ChunkProvider;
import org.bukkit.block.Biome;

/**
 * @author PrettyPonyyy
 *
 */
public class ChunkProviderDoublePerlin extends ChunkProvider
{
	private Perlin m_perlinGenerator;
	private Perlin m_fractalGenerator;

	/*
	 * (non-Javadoc)
	 *
	 * @see org.bukkit.ChunkProvider#onLoad(org.bukkit.World, long)
	 */
	@Override
	public void onLoad(Object world, long seed)
	{
		this.setHasCustomTerrain(true);

		try
		{
			m_perlinGenerator = new Perlin(); //new Perlin();
			m_fractalGenerator = new Perlin(); //new Perlin();

			m_perlinGenerator.setSeed((int)(seed*1024));
			m_perlinGenerator.setOctaveCount(1);
			m_perlinGenerator.setFrequency(1f);

			m_fractalGenerator.setSeed((int)(seed*1024) + 55);
			m_fractalGenerator.setOctaveCount(1);
			m_fractalGenerator.setFrequency(3f);
		}
		catch (Exception e)
		{
		}
	}

	private static double lerp(double a, double b, double f)
	{
		return (a * (1 - f) + b * f);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.bukkit.ChunkProvider#generateChunk(int, int, byte[],
	 * org.bukkit.block.Biome[], double[])
	 */
	@Override
	public void generateChunk(Object world, int X, int Z, byte[] abyte, Biome[] biomes, double[] temperature)
	{
		double density[][][] = new double[16][128][16];

		for (int x = 0; x < 16; x += 3)
		{
			for (int y = 0; y < 128; y += 3)
			{
				for (int z = 0; z < 16; z += 3)
				{
					double posX = x + (X*16);
					double posY = y - 96;
					double posZ = z + (Z*16);

					final double warp = 0.004;
					double warpMod = m_fractalGenerator.getValue(posX * warp, posY * warp, posZ * warp) * 5;
					double warpPosX = posX * warpMod;
					double warpPosY = posY * warpMod;
					double warpPosZ = posZ * warpMod;

					double mod = m_perlinGenerator.getValue(warpPosX * 0.0005, warpPosY * 0.0005, warpPosZ * 0.005);

					density[x][y][z] = -(y - 64);
					density[x][y][z] += mod * 100;
				}
			}
		}

		for (int x = 0; x < 16; x += 3)
		{
			for (int y = 0; y < 128; y += 3)
			{
				for (int z = 0; z < 16; z += 3)
				{
					if (y != 126)
					{
						density[x][y+1][z] = lerp(density[x][y][z], density[x][y+3][z], 0.2);
						density[x][y+2][z] = lerp(density[x][y][z], density[x][y+3][z], 0.8);
					}
				}
			}
		}

		for (int x = 0; x < 16; x += 3)
		{
			for (int y = 0; y < 128; y++)
			{
				for (int z = 0; z < 16; z += 3)
				{
					if (x == 0 && z > 0)
					{
						density[x][y][z-1] = lerp(density[x][y][z], density[x][y][z-3], 0.25);
						density[x][y][z-2] = lerp(density[x][y][z], density[x][y][z-3], 0.85);
					}
					else if (x > 0 && z > 0)
					{
						density[x-1][y][z] = lerp(density[x][y][z], density[x-3][y][z], 0.25);
						density[x-2][y][z] = lerp(density[x][y][z], density[x-3][y][z], 0.85);

						density[x][y][z-1] = lerp(density[x][y][z], density[x][y][z-3], 0.25);
						density[x-1][y][z-1] = lerp(density[x][y][z], density[x-3][y][z-3], 0.25);
						density[x-2][y][z-1] = lerp(density[x][y][z], density[x-3][y][z-3], 0.85);

						density[x][y][z-2] = lerp(density[x][y][z], density[x][y][z-3], 0.25);
						density[x-1][y][z-2] = lerp(density[x][y][z], density[x-3][y][z-3], 0.85);
						density[x-2][y][z-2] = lerp(density[x][y][z], density[x-3][y][z-3], 0.85);
					}
					else if (x > 0 && z == 0)
					{
						density[x-1][y][z] = lerp(density[x][y][z], density[x-3][y][z], 0.25);
						density[x-2][y][z] = lerp(density[x][y][z], density[x-3][y][z], 0.85);
					}
				}
			}
		}

		for (int x = 0; x < 16; x++)
		{
			for (int y = 0; y < 128; y++)
			{
				for (int z = 0; z < 16; z++)
				{
					if (density[x][y][z] > 0)
					{
						abyte[getBlockIndex(x,y,z)] = 1;
					}
					else
					{
						abyte[getBlockIndex(x,y,z)] = 0;
					}
					// Origin point + sand to prevent 5000 years of loading.
					if ((x == 0) && (z == 0) && (X == x) && (Z == z) && (y <= 63)) {
						abyte[getBlockIndex(x,y,z)] = (byte) ((y == 125) ? 12 : 7);
					}
					if(y==1)
						abyte[getBlockIndex(x,y,z)]=7;
				}
			}
		}

		Logger.getLogger("Minecraft").info(String.format("[wat] Chunk (%d,%d)",X,Z));
	}
}
