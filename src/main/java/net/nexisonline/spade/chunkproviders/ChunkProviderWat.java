/**
 * Pony's Handy Dandy Rape Generator
 *
 * INSERT BSD HERE
 */
package net.nexisonline.spade.chunkproviders;

import java.util.Random;
import java.util.logging.Logger;

import libnoiseforjava.module.Perlin;
import libnoiseforjava.module.RidgedMulti;
import net.minecraft.server.BlockSand;
import net.nexisonline.spade.SpadeChunkProvider;
import net.nexisonline.spade.SpadePlugin;
import net.nexisonline.spade.populators.OrePopulator;
import net.nexisonline.spade.populators.SedimentGenerator;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.util.config.ConfigurationNode;

/**
 * @author PrettyPonyyy
 *
 */
public class ChunkProviderWat extends SpadeChunkProvider
{
	public int distanceSquared=-1;
	private static final int WATER_HEIGHT = 32;
	net.minecraft.server.World p=null;

	private Perlin m_perlinGenerator;
	private RidgedMulti m_fractalGenerator;
	private SpadePlugin plugin;
	private SedimentGenerator m_sediment;

	public ChunkProviderWat(SpadePlugin plugin) {
		this.plugin=plugin;
		m_sediment = new SedimentGenerator();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.bukkit.ChunkProvider#onLoad(org.bukkit.World, long)
	 */
	@Override
	public void onLoad(String worldName,long seed, ConfigurationNode node)
	{
		super.onLoad(worldName, seed, node);
		try
		{
			m_perlinGenerator = new Perlin(); //new Perlin();
			m_fractalGenerator = new RidgedMulti(); //new Perlin();

			m_perlinGenerator.setSeed((int)(seed*1024));
			m_perlinGenerator.setOctaveCount(1);
			m_perlinGenerator.setFrequency(1f);

			m_fractalGenerator.setSeed((int)(seed*1024));
			m_fractalGenerator.setOctaveCount(1);
			m_fractalGenerator.setFrequency(0.25f);
			
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
	public byte[] generate(World world, Random random, int X, int Z) {
		byte[] blocks = new byte[16*16*128];
		if(!plugin.shouldGenerateChunk(worldName,X,Z)) {
			return blocks;
		}

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

					double absPosX = Math.abs(posX);
					double absPosY = Math.abs(posY);
					double absPosZ = Math.abs(posZ);

					final double warp = 0.004;
					double warpMod = m_fractalGenerator.getValue(posX * warp, posY * warp, posZ * warp) * 5;
					double warpPosX = absPosX * warpMod;
					double warpPosY = absPosY * warpMod;
					double warpPosZ = absPosZ * warpMod;

					double mod = m_perlinGenerator.getValue(warpPosX * 0.005, warpPosY * 0.005, warpPosZ * 0.005);

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
					byte block = 0;
					if (density[x][y][z] > 0)
					{
						block = 1;
					}
					else
					{
						block = (byte) ((y<WATER_HEIGHT) ? Material.STATIONARY_WATER.getId() : 0);
					}
					// Origin point + sand to prevent 5000 years of loading.
					if ((x == 0) && (z == 0) && (X == x) && (Z == z) && (y <= 63)) {
						block = (byte) ((y == 125) ? 12 : 7);
					}
					if(y==1)
						block=7;
					setBlockByte(blocks,x,y,z,block);
				}
			}
		}

		//Logger.getLogger("Minecraft").info(String.format("[wat] Chunk (%d,%d)",X,Z));
		return blocks;
	}
}
