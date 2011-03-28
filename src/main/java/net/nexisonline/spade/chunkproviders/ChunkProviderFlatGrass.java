package net.nexisonline.spade.chunkproviders;

import java.util.logging.Logger;

import org.bukkit.ChunkProvider;
import org.bukkit.World;
import org.bukkit.block.Biome;

public class ChunkProviderFlatGrass extends ChunkProvider {

	public void onLoad(World world, long seed) {
		this.setHasCustomTerrain(true);
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

}
