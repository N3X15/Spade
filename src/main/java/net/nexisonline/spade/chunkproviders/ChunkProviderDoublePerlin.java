/**
 * Pony's Handy Dandy Rape Generator
 *
 * INSERT BSD HERE
 */
package net.nexisonline.spade.chunkproviders;

import java.util.logging.Logger;

import libnoiseforjava.module.Perlin;
import net.minecraft.server.BlockSand;
import net.nexisonline.spade.InterpolatedDensityMap;
import net.nexisonline.spade.SpadeChunkProvider;
import net.nexisonline.spade.SpadePlugin;
import net.nexisonline.spade.generators.OrePopulator;
import net.nexisonline.spade.generators.SedimentGenerator;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.util.config.Configuration;
import org.bukkit.util.config.ConfigurationNode;

import toxi.math.noise.SimplexOctaves;

/**
 * @author PrettyPonyyy
 *
 */
public class ChunkProviderDoublePerlin extends SpadeChunkProvider
{
	private static final int WATER_HEIGHT = 32;
	net.minecraft.server.World p=null;
	
	private SpadePlugin plugin;
	private SimplexOctaves m_simplexGenerator;
	private SimplexOctaves m_simplexGenerator2;
	private OrePopulator m_populator;
	private SedimentGenerator m_sediment;
	public ChunkProviderDoublePerlin(SpadePlugin plugin) {
		this.plugin=plugin;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.bukkit.ChunkProvider#onLoad(org.bukkit.World, long)
	 */
	@Override
	public void onLoad(Object world, long seed)
	{
		this.setHasCustomTerrain(true);
		this.setHasCustomSedimenter(true);
		this.setHasCustomPopulator(true);

		try {
			this.p = (net.minecraft.server.World)world;
		} catch(Throwable e) {}

		try
		{
			new Perlin();
			new Perlin();

			m_simplexGenerator=new SimplexOctaves(1234,4);
			m_simplexGenerator2=new SimplexOctaves(1234+51,4);
			
			/*
			m_perlinGenerator.setSeed(1234);
			m_perlinGenerator.setOctaveCount(1);
			m_perlinGenerator.setFrequency(1f);

			m_fractalGenerator.setSeed(1235);
			m_fractalGenerator.setOctaveCount(1);
			m_fractalGenerator.setFrequency(2f);
			*/

			m_populator = new OrePopulator(plugin, plugin.getServer().getWorld(p.worldData.name),null,seed);
			m_sediment = new SedimentGenerator(plugin, plugin.getServer().getWorld(p.worldData.name),null,seed);
		}
		catch (Exception e)
		{
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.bukkit.ChunkProvider#generateChunk(int, int, byte[],
	 * org.bukkit.block.Biome[], double[])
	 */
	@Override
	public void generateChunk(World world, int X, int Z, byte[][][] blocks, Biome[][] biomes, double[][] temperature)
	{
		if(!plugin.shouldGenerateChunk(worldName,X,Z))
		{
				Logger.getLogger("Minecraft").info(String.format("[DoublePerlin] SKIPPING Chunk (%d,%d)",X,Z));
				return;
		}
		
		InterpolatedDensityMap density = new InterpolatedDensityMap();

		for (int x = 0; x < 16; ++x)
		{
			for (int y = 0; y < 128; y += 16)
			{
				for (int z = 0; z < 16; ++z)
				{
					double posX = (x + (X*16));
					double posY = (y - 96);
					double posZ = (z + (Z*16));

					final double warp = 0.004;
					double warpMod = m_simplexGenerator.noise(posX * warp, posY * warp, posZ * warp) * 5;//m_fractalGenerator.getValue(posX * warp, posY * warp, posZ * warp) * 5;
					double warpPosX = posX * warpMod;
					double warpPosY = posY * warpMod;
					double warpPosZ = posZ * warpMod;

					double mod = m_simplexGenerator2.noise(warpPosX * 0.0005, warpPosY * 0.0005, warpPosZ * 0.005);//m_perlinGenerator.getValue(warpPosX * 0.0005, warpPosY * 0.0005, warpPosZ * 0.005);

					density.setDensity(x, y, z, (-(y - 64))+ mod * 100);
				}
			}
		}
		density.interpolate();
		for (int x = 0; x < 16; x++)
		{
			for (int y = 0; y < 128; y++)
			{
				for (int z = 0; z < 16; z++)
				{
					byte block = 0;
					if ((int)density.getDensity(x,y,z) > 5)
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
					blocks[x][y][z]=block;
				}
			}
		}
		//Logger.getLogger("Minecraft").info(String.format("[DoublePerlin %d] Chunk (%d,%d)",m_perlinGenerator.getSeed(),X,Z));
	}
	

	@Override
	public void generateSediment(World world, int X, int Z, byte[][][] blocks, Biome[][] biomes) {
		if(!plugin.shouldGenerateChunk(worldName,X,Z)) {
			return;
		}		
		BlockSand.a=true;
		m_sediment.addToProtochunk(blocks,X,Z,biomes);
		BlockSand.a=false;
	}
	
	@Override
	public void populateChunk(World world,int X, int Z) {
		if(!plugin.shouldGenerateChunk(worldName,X,Z)) {
			return;
		}
		
		BlockSand.a = true;
		m_populator.addToChunk(world.getChunkAt(X,Z),X,Z);
		BlockSand.a = false;
	}

	@Override
	public ConfigurationNode configure(ConfigurationNode node) {
		if(node==null) {
			node = Configuration.getEmptyNode();
		}
		return node;
	}
}
