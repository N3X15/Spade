package net.nexisonline.spade.chunkproviders;

import java.util.logging.Logger;

import libnoiseforjava.module.Perlin;
import libnoiseforjava.module.RidgedMulti;
import net.nexisonline.spade.SpadeChunkProvider;

import org.bukkit.block.Biome;
import org.bukkit.util.config.ConfigurationNode;

public class ChunkProviderSurrealIslands extends SpadeChunkProvider {
	private RidgedMulti terrainNoise;
	private Perlin caveNoise;
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.bukkit.ChunkProvider#onLoad(org.bukkit.World, long)
	 */
	@Override
	public void onLoad(Object world, long seed) {
		this.setHasCustomTerrain(true);

		try {
			terrainNoise = new RidgedMulti();
			caveNoise=new Perlin();
			
			terrainNoise.setSeed((int) seed);
			caveNoise.setSeed((int) seed*16);

			//terrainNoise.setFrequency(Frequency);
			//terrainNoise.setNoiseQuality(noiseQuality);
			terrainNoise.setOctaveCount(3);
			//terrainNoise.setLacunarity(Lacunarity);

			//caveNoise.setFrequency(Frequency);
			//caveNoise.setNoiseQuality(noiseQuality);
			caveNoise.setOctaveCount(7);
			//caveNoise.setLacunarity(Lacunarity);
		} catch (Exception e) {
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.bukkit.ChunkProvider#generateChunk(int, int, byte[],
	 * org.bukkit.block.Biome[], double[])
	 */
	@Override
	public void generateChunk(Object world, int X, int Z, byte[] abyte, Biome[] biomes,
			double[] temperature) {

		for (int x = 0; x < 16; x+=1) {
			for (int z = 0; z < 16; z+=1) {
				for (int y = 0; y < 128; y+=1) {
					//byte block = (byte) (blockIsSolid(x+(X*16),y,z+(Z*16)) ? 1 : 0);
					byte block = (byte)(terrainNoise.getValue(x+(X*16), 0 , z+(Z*16)) / ((double)y/64d) < -0.9d ? 1 : 0);
					// If below height, set rock. Otherwise, set air.
					block = (y <= 63 && block == 0) ? (byte) 9 : block; // Water

					// Origin point + sand to prevent 5000 years of loading.
					if(x==0&&z==0&&X==x&&Z==z&&y<=63)
						block=(byte) ((y==63)?12:7);

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
					abyte[getBlockIndex(x,y,z)]=block;//(byte) ((y<2) ? Material.BEDROCK.getId() : block);
				}
			}
		}
		Logger.getLogger("Minecraft").info(String.format("[Islands] Chunk (%d,%d)",X,Z));

	}
	boolean blockIsSolid(int x, int y, int z)
	{
		float noise, density, center_falloff, plateau_falloff;

		/* caves */
		//if (Math.pow(caveNoise.getValue((double)x * 5.0d, (double)y * 5.0d,
				//(double)z * 5.0d), 3.0d) < -0.5d)
			//return false;

		/* falloff from the top */
		if (y > 115) // 128*0.9
			return false;
		else if (y > 102)
			plateau_falloff = 128 - (y - 102) * 1280; // 10f;
		else
			plateau_falloff = 128;// 1f

		/* falloff from center */
		center_falloff = (float) (128 / (
				Math.pow((x - 64) * 128, 2.0) +
				Math.pow((y - 128) * 102, 2.0) +
				Math.pow((z - 64) * 128, 2.0)
		));

		/* noise combined density */
		noise = (float) terrainNoise.getValue(x, y * 0.5, z);
		density = noise * center_falloff * plateau_falloff;
		return density > 0.2f;
	}

	@Override
	public ConfigurationNode configure(ConfigurationNode node) {
		return node;
		// TODO Auto-generated method stub
		
	}
}
