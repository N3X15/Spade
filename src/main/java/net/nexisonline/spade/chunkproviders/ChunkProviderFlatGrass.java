package net.nexisonline.spade.chunkproviders;

import java.util.logging.Logger;

import org.bukkit.ChunkProvider;
import org.bukkit.World;
import org.bukkit.block.Biome;

public class ChunkProviderFlatGrass extends ChunkProvider {

	public void onLoad(World world, long seed) {
		
	}

	@Override
	public void generateChunk(World world, int X, int Z, byte[] abyte,
			Biome[] biomes, double[] temperature) {
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				double h = 63;
				for (int y = 0; y < h + 63; y++) {
					abyte[getBlockIndex(x, y, z)] = (byte) ((y < 2) ? 7 : 1);
				}
			}
		}
		Logger.getLogger("Minecraft")
				.info(String.format("Chunk (%d,%d)", X, Z));
	}

	@Override
	public void populateChunk(World world, int x, int z, byte[] abyte,
			Biome[] biomes) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean hasCustomTerrainGenerator() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean hasCustomPopulator() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasCustomCaves() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void generateCaves(World world, int x, int z, byte[] abyte) {
		// TODO Auto-generated method stub

	}

}
