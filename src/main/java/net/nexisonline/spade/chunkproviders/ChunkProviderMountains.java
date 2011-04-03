/**
 * N3X15's Handy Dandy Mountain Generator
 * 	From MineEdit
 * 
 * INSERT BSD HERE
 */
package net.nexisonline.spade.chunkproviders;

import java.util.logging.Logger;

import libnoiseforjava.NoiseGen.NoiseQuality;
import libnoiseforjava.module.Perlin;
import libnoiseforjava.module.RidgedMulti;
import net.nexisonline.spade.Heightmap;
import net.nexisonline.spade.Interpolator;

import org.bukkit.ChunkProvider;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;

/**
 * @author N3X15
 * 
 */
public class ChunkProviderMountains extends ChunkProvider {
	private RidgedMulti terrainNoise;
	private Perlin continentNoise;
	private int continentNoiseOctaves = 16;
	private final NoiseQuality noiseQuality = NoiseQuality.QUALITY_STD;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.bukkit.ChunkProvider#onLoad(org.bukkit.World, long)
	 */
	@Override
	public void onLoad(Object world, long seed) {
		setHasCustomTerrain(true);

		final double Frequency = 0.1;
		final double Lacunarity = 0.05;
		final int OctaveCount = continentNoiseOctaves = 4;

		try {
			terrainNoise = new RidgedMulti();
			continentNoise = new Perlin();
			terrainNoise.setSeed((int) seed);
			continentNoise.setSeed((int) seed + 2);

			terrainNoise.setFrequency(Frequency);
			terrainNoise.setNoiseQuality(noiseQuality);
			terrainNoise.setOctaveCount(OctaveCount);
			terrainNoise.setLacunarity(Lacunarity);

			// continentNoise.setFrequency(ContinentNoiseFrequency);
			// continentNoise.setNoiseQuality(noiseQuality);
			continentNoise.setOctaveCount(continentNoiseOctaves);
			// continentNoise.setLacunarity(Lacunarity);
			// continentNoise.setPersistence(Persistance);
		} catch (final Exception e) {
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.bukkit.ChunkProvider#generateChunk(int, int, byte[],
	 * org.bukkit.block.Biome[], double[])
	 */
	@Override
	public void generateChunk(Object world, int X, int Z, byte[] abyte,
			Biome[] biomes, double[] temperature) {
		int minHeight = Integer.MAX_VALUE;
		Heightmap hm = new Heightmap();
		for (int x = 0; x < 16; x += 3) {
			for (int z = 0; z < 16; z += 3) {
				// Generate our continental noise.
				hm.set(x,z,continentNoise.getValue(
						(x + (X * 16)) * 0.01,
						(z + (Z * 16)) * 0.01, 0));// *5d; // 2.0
				
			}
		}
		hm=Interpolator.LinearExpandHeightmap(hm);

		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				final int height = (int) ((hm.get(x, z) * 32d) + 96d);
				if (height < minHeight) {
					minHeight = height;
				}
				for (int y = 0; y < 128; y++) {
					
					byte block = (y <= height) ? (byte) 1 : (byte) 0; // Fill;
					block = (block > 0) ? (byte) 1 : (byte) 0;
					// If below height, set rock. Otherwise, set air.
					block = ((y <= 63) && (block == 0)) ? (byte) 9 : block; // Water

					// Origin point + sand to prevent 5000 years of loading.
					if ((x == 0) && (z == 0) && (X == x) && (Z == z) && (y <= 63)) {
						block = (byte) ((y == 63) ? 12 : 7);
					}

					// Old cave stuff, handled by CraftBukkit now.
					// double _do = ((CaveNoise.GetValue(x + (X * chunksize.X),
					// z + (Z * chunksize.Z), y * CaveDivisor) + 1) / 2.0);
					// bool d3 = _do > CaveThreshold;

					// if(d3)
					// {
					// if (y <= 63)
					// block = 3;
					// else
					// block = 0;
					// }
					// else
					// block = (d3) ? b[x, y, z] : (byte)1;
					abyte[getBlockIndex(x, y, z)] = (byte) ((y < 2) ? Material.BEDROCK
							.getId() : block);
				}
			}
		}
		Logger.getLogger("Minecraft").info(
				String.format("[Mountains] Chunk (%d,%d) Min Height: %dm", X,
						Z, minHeight));

	}
}
