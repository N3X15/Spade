/**
 * Pony's Handy Dandy Rape Generator 2.0
 *
 * INSERT BSD HERE
 */
package net.nexisonline.spade.chunkproviders;

import net.minecraft.server.WorldServer;
import net.nexisonline.spade.InterpolatedDensityMap;
import net.nexisonline.spade.SpadeChunkProvider;
import net.nexisonline.spade.SpadePlugin;
import net.nexisonline.spade.generators.DungeonPopulator;
import net.nexisonline.spade.generators.OrePopulator;
import net.nexisonline.spade.generators.PonyCaveGenerator;
import net.nexisonline.spade.generators.SedimentGenerator;
import net.nexisonline.spade.generators.StalactiteGenerator;

import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.util.config.Configuration;
import org.bukkit.util.config.ConfigurationNode;

import toxi.math.noise.SimplexNoise;

/**
 * 
 * @author PrettyPonyyy
 * 
 * 
 */
public class ChunkProviderSurrealIslands extends SpadeChunkProvider {
	private static final int WATER_HEIGHT = 32;
	private static final int OCEAN_FLOOR=16;
	net.minecraft.server.World p = null;
	private SpadePlugin plugin;
	SimplexNoise m_simplex1;
	SimplexNoise m_simplex2;
	SimplexNoise m_xTurbulence;
	SimplexNoise m_yTurbulence;
	SimplexNoise m_zTurbulence;
	private SimplexNoise m_SeaFloorNoise;
	private PonyCaveGenerator mCaves;
	private DungeonPopulator m_Dungeons;
	private InterpolatedDensityMap density;
	private StalactiteGenerator stalactites;
	private SedimentGenerator m_sedimentGenerator;
	private OrePopulator m_populator;

	public ChunkProviderSurrealIslands(SpadePlugin plugin) {
		this.plugin = plugin;
		density = new InterpolatedDensityMap();
	}

	/*
	 * 
	 * (non-Javadoc)
	 * 
	 * 
	 * 
	 * @see org.bukkit.ChunkProvider#onLoad(org.bukkit.World, long)
	 */
	@Override
	public void onLoad(Object world, long seed) {
		this.setHasCustomTerrain(true);
		this.setHasCustomSedimenter(true);
		this.setHasCustomPopulator(true);
		this.setHasCustomCaves(true);
		try {
			this.p = (net.minecraft.server.World) world;
		} catch (Throwable e) {
		}
		try {
			m_simplex1 = new SimplexNoise(((int) seed * 1024));
			m_simplex1.setFrequency(0.005);
			m_simplex1.setAmplitude(50);

			m_simplex2 = new SimplexNoise(((int) seed * 1024) + 1);
			m_simplex2.setFrequency(0.0005);
			m_simplex2.setAmplitude(25);

			m_xTurbulence = new SimplexNoise(((int) seed * 1024) + 2);
			m_xTurbulence.setFrequency(0.05);
			m_xTurbulence.setAmplitude(5);

			m_yTurbulence = new SimplexNoise(((int) seed * 1024) + 3);
			m_yTurbulence.setFrequency(0.05); 
			m_yTurbulence.setAmplitude(5);

			m_zTurbulence = new SimplexNoise(((int) seed * 1024) + 4);
			m_zTurbulence.setFrequency(0.05);
			m_zTurbulence.setAmplitude(5);
			
			m_SeaFloorNoise = new SimplexNoise(((int)seed*1024)+4);
			
			stalactites = new StalactiteGenerator(plugin, plugin.getServer().getWorld(p.worldData.name),null,seed);
			m_Dungeons = new DungeonPopulator(plugin, plugin.getServer().getWorld(p.worldData.name),null,seed);
			mCaves = new PonyCaveGenerator(seed);
			m_populator = new OrePopulator(plugin, plugin.getServer().getWorld(p.worldData.name),null,seed);
			
		} catch (Exception e) {
		}
	}

	/*
	 * 
	 * (non-Javadoc)
	 * 
	 * 
	 * 
	 * @see org.bukkit.ChunkProvider#generateChunk(int, int, byte[],
	 * 
	 * org.bukkit.block.Biome[], double[])
	 */
	@Override
	public void generateChunk(Object world, int X, int Z, byte[] blocks,
			Biome[] biomes, double[] temperature) {
		/*
		 * if(!plugin.shouldGenerateChunk(worldName,X,Z))
		 * 
		 * {
		 * 
		 * // Never do this, var gets passed by-val instead of by-ref.
		 * 
		 * //blocks=new byte[blocks.length];
		 * 
		 * Logger.getLogger("Minecraft").info(String.format(
		 * "[Islands] SKIPPING Chunk (%d,%d)",X,Z));
		 * 
		 * return;
		 * 
		 * }
		 */
		double frequency = 0;
		double amplitude = 0;
		for (int x = 0; x < 16; x+=5) {
			for (int y = 0; y <= 128; y += 16) {
				for (int z = 0; z < 16; z+=5) {
					double posX = (x + (X * 16));
					double posY = (y - 64);
					double posZ = (z + (Z * 16));
					frequency = 0.05;
					amplitude = 10;
					double warpX = posX + m_xTurbulence.sample(posX, posY, posZ, frequency, amplitude);
					double warpY = posY + m_yTurbulence.sample(posX, posY, posZ, frequency, amplitude);
					double warpZ = posZ + m_zTurbulence.sample(posX, posY, posZ, frequency, amplitude);
					double d = -12;
					frequency = 0.005;
					amplitude = 50;
					d -= m_simplex1.sample(warpX, warpY, warpZ, frequency, amplitude);
					frequency = 0.0005;
					amplitude = 25;
					d -= m_simplex2.sample(warpX, warpY, warpZ, frequency, amplitude);
					density.setDensity(x, y, z, d);
				}
			}
		}
		density.interpolate();
		double minDensity = Double.MAX_VALUE;
		double maxDensity = Double.MIN_VALUE;
		for (int x = 0; x < 16; ++x) {
			for (int y = 0; y < 128; ++y) {
				for (int z = 0; z < 16; ++z) {
					byte block = 0;
					double d = density.getDensity(x, y, z);
					maxDensity = Math.max(maxDensity, d);
					minDensity = Math.min(minDensity, d);
					if ((int) d > 5) {
						block = 1;
					} else {
						block = (byte) ((y < WATER_HEIGHT) ? Material.STATIONARY_WATER.getId() : 0);
					}
					if(y<=OCEAN_FLOOR+m_SeaFloorNoise.sample((x + (X * 16)), (z + (Z * 16)),0.01,5) && (block==Material.STATIONARY_WATER.getId() || block==Material.WATER.getId())) {
						block=1;
					}
					if (y == 1)
						block = 7;
					blocks[getBlockIndex(x, y, z)] = block;
				}
			}
		}
/*
		Logger.getLogger("Minecraft").info(
				String.format(
						"[Islands] Chunk (%d,%d) (densityRange= [%.2f,%.2f])",
						X, Z, minDensity, maxDensity));
*/
	}

	@Override
	public void generateCaves(Object world, int X, int Z, byte[] data) {
		mCaves.generateCaves(world, X, Z, data);
	}

	/**
	 * 
	 * Stolen standard terrain populator, screwed with to generate water at the
	 * desired height.
	 */
	@Override
	public void generateSediment(Object world, int X, int Z, byte[] blocks,
			Biome[] biomes) {
		/*
		 * if(!plugin.shouldGenerateChunk(worldName,X,Z)) {
		 * 
		 * Logger.getLogger("Minecraft").info(String.format(
		 * "[Islands] SKIPPING generateSediment on Chunk (%d,%d)",X,Z));
		 * 
		 * return;
		 * 
		 * }
		 */
		m_sedimentGenerator.addToChunk(((WorldServer)world).getWorld().getChunkAt(X,Z), X, Z);
	}

	public void populateChunk(Object ch, int X, int Z) {
		if(getBukkitWorld().isChunkLoaded(X, Z)) {
			stalactites.addToChunk(getBukkitWorld().getChunkAt(X, Z), X, Z);
			m_populator.addToChunk(getBukkitWorld().getChunkAt(X, Z),X,Z);
			m_Dungeons.addToChunk(getBukkitWorld().getChunkAt(X, Z), X, Z);
		}
	}

	private CraftWorld getBukkitWorld() {
		// TODO Auto-generated method stub
		return ((WorldServer)p).getWorld();
	}

	@Override
	public ConfigurationNode configure(ConfigurationNode node) {
		if (node == null) {
			node = Configuration.getEmptyNode();
		}
		return node;
	}

	@Override
	public boolean canSpawnAt(org.bukkit.World w, int x, int z) {
		return p.getMaterial(x, p.getHighestBlockYAt(x, z), z).isSolid();
	}
}
